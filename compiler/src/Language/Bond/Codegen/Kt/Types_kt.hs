-- Copyright (c) Microsoft. All rights reserved.
-- Licensed under the MIT license. See LICENSE file in the project root
-- for full license information.

{-# LANGUAGE QuasiQuotes, OverloadedStrings, RecordWildCards #-}

module Language.Bond.Codegen.Kt.Types_kt
    ( types_kt
    , KtFieldMapping(..)
    , KtStructMapping(..)
    ) where

import Data.Monoid
import Prelude
import Data.Text.Lazy (Text)
import Text.Shakespeare.Text
import Language.Bond.Syntax.Types
import Language.Bond.Util
import Language.Bond.Codegen.TypeMapping
import Language.Bond.Codegen.Util
import qualified Language.Bond.Codegen.Kt.Util as KT

-- Type mapping rules:
-- 1. A Bond struct is mapped to Kotlin open class type.
-- 2. All fields are mapped to mutable `var' variables.
-- 3. A nullable field is mapped to T? type.
-- 4. A non-nullable field but set to 'nothing' is mapped to T? type.
-- 5. Type alias is resolved to reuse the original type.
--    (Because Kotlin does not support type alias yet, though you can
--    see reference in its own syntax reference PDF doc).
--
-- Annotations are used in the following fields:
-- 1. All open classes are marked with BondSchema and BondGeneratedCode().
-- 2. All fields are marekd with BondFieldId().
-- 3. Nullables type is marked with BondNullable(T::class)
-- 4. string type is marked with BondByteString
-- 5. Customized attributes are converted into: BondAttribute(attrName, value)
-- 6. Bond's required keyword is converted into BondRequired annotation.
-- 7. Bond's required_optional keyword is converted into
--    BondRequiredOptional annotation.
-- 
-- All annotations are defined in com.microsoft.bond.annotations
-- package.
--
-- The definitions can be found from files below:
--
-- * Kt/Util.hs: 1, 2, 5, 6, 7
-- * Codegen/TypeMapping.hs: 3, 4
--
-- Generic
-- 1. Value type constraint is ignored (struct T<K: value>)
--
-- Note
-- 1. Data class is not applicable to represent Bond struct because
--
--    a) Bond struct can have zero field, while data class requires at
--       least one constructor parameter.
--    b) Bond struct can be inherited, while data class does not allow
--       inheritance.


-- | Kotlin representation of schema structs
data KtStructMapping =
    KtClass                   -- ^ open class
    deriving Eq

-- | Representation of schema fields in the generated C# types
data KtFieldMapping =
    KtPublicFields            -- ^ public fields
    deriving Eq

-- | Codegen template for generating definitions of C# types representing the schema.
types_kt
    :: KtStructMapping        -- ^ Specifies how to represent schema structs
    -> KtFieldMapping         -- ^ Specifies how to represent schema fields
    -> MappingContext -> String -> [Import] -> [Declaration] -> (String, Text)
types_kt structMapping _ kt _ _ declarations = (fileSuffix, [lt|

package #{ktPackage}

import com.microsoft.bond.annotations.*
import com.microsoft.bond.types.*

#{doubleLineSep 1 typeDefinition declarations}

// End of package #{ktPackage}
|])
  where
    -- Kotlin type
    ktType = getTypeName kt

    -- Kotlin package name is always mapped from Bond namespace.
    ktPackage = sepBy "." toText $ getNamespace kt

    fileSuffix = case structMapping of
        _ -> "_types.kt"

    struct = case structMapping of
        _ -> [lt|open class|]

    typeAttributes s = case structMapping of
        _ -> KT.typeAttributes kt s

    propertyAttributes f = case structMapping of
        KtClass -> KT.propertyAttributes kt f

    -- Kotlin type definition for schema struct
    typeDefinition s@Struct {..} = [lt|
#{typeAttributes s}
#{struct} #{declName}#{params}#{maybe interface baseClass structBase}
{
    #{doubleLineSep 1 property structFields}
}|]
      where
        interface = case structMapping of
            _ -> mempty

        -- type parameters: Right now we limit all type parameter
        -- as subtype of Any, so we can create instance for any type.
        params = angles $ sepBy ", " paramNameAsKotlinAny declParams

        paramNameAsKotlinAny :: TypeParam -> String
        paramNameAsKotlinAny tp = paramName tp ++ ": Any"

        baseClass x = [lt| : #{ktType x}()|]

        -- default value
        ktDefault = KT.defaultValue kt

        -- Kotlin properties (as parameters of primary constructor)
        -- TODO We may consider supporting readonly parameter in the
        -- future.
        property f@Field {..} = [lt|#{propertyAttributes f} var #{fieldName} : #{ktType fieldType}#{genericTypeToNullable fieldType} #{optional initializerValue $ ktDefault f}|]

        genericTypeToNullable :: Type -> String
        genericTypeToNullable f = case f of
          (BT_TypeParam _) -> "?"
          _ -> ""

        initializerValue x = [lt|= #{x}|]

    typeDefinition e@Enum {..} = [lt|#{KT.typeAttributes kt e}
enum class #{declName}(val num : Int)
{
    #{newlineSep 1 constant enumConstantsWithInt}
}|]
      where
        -- constant
        constant Constant {..} = let value x = [lt|(#{x})|] in
            [lt|#{constantName}#{optional value constantValue},|]

        -- Process constants to make sure all values can be converted
        -- to integer.
        enumConstantsWithInt = fixEnumWithInt 0 enumConstants []

        fixEnumWithInt :: Int -> [Constant] -> [Constant] -> [Constant]
        fixEnumWithInt _ [] result = reverse result
        fixEnumWithInt nextInt (h:r) result = case constantValue h of
          Just n -> fixEnumWithInt (n + 1) r (h:result)
          _ -> fixEnumWithInt (nextInt + 1) r ((Constant (constantName h) (Just nextInt)):result)

    typeDefinition _ = mempty
