-- Copyright (c) Microsoft. All rights reserved.
-- Licensed under the MIT license. See LICENSE file in the project root for full license information.

{-# LANGUAGE QuasiQuotes, OverloadedStrings, RecordWildCards #-}

module Language.Bond.Codegen.Kt.Util
    ( typeAttributes
    , propertyAttributes
    , schemaAttributes
    , defaultValue
    ) where

import Data.Int (Int64)
import Data.Monoid
import Prelude
import Data.Text.Lazy (Text)
import Text.Shakespeare.Text
import Paths_bond (version)
import Data.Version (showVersion)
import Language.Bond.Syntax.Types
import Language.Bond.Syntax.Util
import Language.Bond.Codegen.TypeMapping
import Language.Bond.Codegen.Util

-- Kotlin field/property attributes
propertyAttributes :: MappingContext -> Field -> Text
propertyAttributes kt Field {..} =
    schemaAttributes 2 fieldAttributes
 <> [lt|@BondFieldId(#{fieldOrdinal})#{typeAttribute}#{modifierAttribute fieldType fieldModifier}|]
        where
            annotatedType = getAnnotatedTypeName kt fieldType
            propertyType = getTypeName kt fieldType
            typeAttribute = if annotatedType /= propertyType
                then [lt| #{annotatedType}|]
                else mempty
            modifierAttribute BT_MetaName _ = [lt| @BondRequiredOptional|]
            modifierAttribute BT_MetaFullName _ = [lt| @BondRequiredOptional|]
            modifierAttribute _ Required = [lt| @BondRequired|]
            modifierAttribute _ RequiredOptional = [lt| @BondRequiredOptional|]
            modifierAttribute _ _ = mempty

-- Kotlin class/struct/interface attributes
typeAttributes :: MappingContext -> Declaration -> Text
typeAttributes kt s@Struct {..} = optionalTypeAttributes kt s <> [lt|
@BondSchema |] <> generatedCodeAttr
typeAttributes kt e@Enum {..} = optionalTypeAttributes kt e <> [lt|
@BondSchema |] <> generatedCodeAttr
typeAttributes _ Service {..} = "Kotlin:typeAttributes: Service is not supported yet."
typeAttributes _ _ = error "Kotlin:typeAttributes: impossible happened."

-- Kotlin service attributes will be implemented in the future.

generatedCodeAttr :: Text
generatedCodeAttr = [lt|
@BondGeneratedCode("gbc", "#{showVersion version}")|]

idl :: MappingContext
idl = MappingContext idlTypeMapping [] [] []  

optionalTypeAttributes :: MappingContext -> Declaration -> Text
optionalTypeAttributes _ decl =
    schemaAttributes 1 (declAttributes decl)

-- Attributes defined by the user in the schema
schemaAttributes :: Int64 -> [Attribute] -> Text
schemaAttributes indent = newlineSepEnd indent schemaAttribute
  where
    schemaAttribute Attribute {..} =
        [lt| @BondAttribute("#{getQualifiedName idl attrName}", "#{attrValue}")|]

-- Initial value for Kotlin property or Nothing
defaultValue :: MappingContext -> Field -> Maybe Text
defaultValue kt Field {fieldDefault = Nothing, ..} = implicitDefault fieldType
  where
    newInstance t = Just [lt|#{getInstanceTypeName kt t}()|]
    implicitDefault (BT_Bonded t) = Just [lt|Bonded<#{getTypeName kt t}>()|]
    -- We can't really initialize a default object for generic type
    -- field due to the limitation of both Java and Kotlin.
    -- I'm still looking for a solution. Please let me know if you has
    -- any suggestions.
    implicitDefault (BT_TypeParam _) = Just [lt|null|]
    implicitDefault t@BT_Blob = newInstance t
    implicitDefault t@BT_UInt8 = newInstance t
    implicitDefault t@BT_UInt16 = newInstance t
    implicitDefault t@BT_UInt32 = newInstance t
    implicitDefault t@BT_UInt64 = newInstance t
    implicitDefault (BT_Nullable _) = Just[lt|null|]
    implicitDefault BT_Int8 = Just [lt|0|]
    implicitDefault BT_Int16 = Just [lt|0|]
    implicitDefault BT_Int32 = Just [lt|0|]
    implicitDefault BT_Int64 = Just [lt|0L|]
    implicitDefault BT_Float = Just [lt|0.0f|]
    implicitDefault BT_Double = Just [lt|0.0|]
    implicitDefault BT_String = Just [lt|ByteString("")|]
    implicitDefault BT_WString = Just [lt|""|]
    implicitDefault (BT_List _) = Just [lt|listOf()|]
    implicitDefault (BT_Set _) = Just [lt|setOf()|]
    implicitDefault (BT_Map _ _) = Just [lt|mapOf()|]
    implicitDefault (BT_Vector _) = Just [lt|arrayOf()|]
    implicitDefault t@(BT_UserDefined a@Alias {..} args)
        | customAliasMapping kt a = newInstance t
        | otherwise = implicitDefault $ resolveAlias a args
    implicitDefault t
        | isStruct t = newInstance t
    implicitDefault _ = Nothing

defaultValue kt Field {fieldDefault = (Just def), ..} = explicitDefault def
  where
    explicitDefault (DefaultInteger x) = Just $ intLiteral fieldType x
      where
        intLiteral BT_Int8 value = [lt|#{value}|]
        intLiteral BT_Int16 value = [lt|#{value}|]
        intLiteral BT_Int32 value = [lt|#{value}|]
        intLiteral BT_Int64 value = [lt|#{value}|]
        intLiteral BT_UInt8 value = [lt|UnsignedByte(#{value})|]
        intLiteral BT_UInt16 value = [lt|UnsignedShort(#{value})|]
        intLiteral BT_UInt32 value = [lt|UnsignedInt(#{value})|]
        intLiteral BT_UInt64 value = [lt|UnsignedLong(#{value})|]
        intLiteral _ _ = error "Kotlin:Int:defaultValue/floatLiteral: impossible happened."
    explicitDefault (DefaultFloat x) = Just $ floatLiteral fieldType x
      where
        floatLiteral BT_Float y = [lt|#{y}f|]
        floatLiteral BT_Double y = [lt|#{y}|]
        floatLiteral _ _ = error "Kotlin:Float:defaultValue/floatLiteral: impossible happened."
    explicitDefault (DefaultBool True) = Just "true"
    explicitDefault (DefaultBool False) = Just "false"
    explicitDefault (DefaultString x) = Just $ strLiteral fieldType x
      where
        strLiteral BT_String value = [lt|ByteString("#{value}")|]
        strLiteral BT_WString value = [lt|"#{value}"|]
        strLiteral _ _ = error "Kotlin:Str:defaultValue/floatLiteral: impossible happened."
    explicitDefault DefaultNothing = Just [lt|null|]
    -- TODO Enum is still incorrect
    explicitDefault (DefaultEnum x) = Just [lt|#{getTypeName kt fieldType}.#{x}|]
