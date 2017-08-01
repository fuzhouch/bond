// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ObjectCreator")

package qbranch.utils.generic

import kotlin.reflect.KClass
import qbranch.exceptions.UnsupportedBondTypeException
import qbranch.utils.isGenericClass
import qbranch.utils.isBondGeneratedStruct

private class UnknownClassObjectCreator(objectClass : Class<*>) : ObjectCreatorAsAny {
    val cls = objectClass
    override fun newInstanceAsAny(): Any {
        return cls.newInstance()
    }
}

private class ConcreteTypeObjectCreator<T>(objectClass : Class<T>) : ObjectCreatorAsConcreteType<T> {
    val cls = objectClass
    override fun newInstanceAsAny(): Any = newInstance() as Any
    override fun newInstance() : T = cls.newInstance()
}

private class GenericTypeObjectCreator<T>(objectClass : Class<T>, genericTypeParameters : Array<ObjectCreatorAsAny>)
    : ObjectCreatorAsConcreteType<T> {

    val cls = objectClass
    val typeParams = genericTypeParameters

    override fun newInstanceAsAny(): Any = newInstance() as Any

    override fun newInstance() : T {
        // I seem failures when calling a Kotlin constructor with
        // vararg in Kotlin 1.0. If we pass *typeParams as array,
        // Kotlin constructor can only receive the first object,
        // thus always complains NoSuchMethodException.
        //
        // I didn't find a way to fix it. Instead, I can only
        // workaround it by letting generated definition
        // (see unit test: StructWithGenericField)
        // accept an Array<ObjectCreatorAsAny> as only parameter.
        //
        // Let's see if further version can fix it.
        return cls.getConstructor(Array<ObjectCreatorAsAny>::class.java).newInstance(typeParams)
    }
}

/**
 * A helper function to create an ObjectCreator instance for non-generic type.
 * @param objectClass Class object of given non-generic object.
 * @return An ObjectCreatorAsConcreteType<T> object to allow calling newInstance().
 */
fun<T> build(objectClass : Class<T>) : ObjectCreatorAsConcreteType<T> {
    if (!objectClass.isBondGeneratedStruct()) {
        throw UnsupportedBondTypeException(objectClass)
    }

    if (objectClass.isGenericClass()) {
        throw UnsupportedBondTypeException(objectClass)
    }

    return ConcreteTypeObjectCreator(objectClass)
}

/**
 * A helper function to create an ObjectCreator instance for non-generic Kotlin type.
 * @param objectClass Class object of given non-generic object.
 * @return An ObjectCreatorAsConcreteType<T> object to allow calling newInstance().
 */
fun<T: Any> build(objectClass : KClass<T>) : ObjectCreatorAsConcreteType<T> {
    val jClass = objectClass.java
    return build(jClass)
}

/**
 * A helper function to create an ObjectCreator instance for a generic type.
 * @param genericClass Class object of given generic class definition.
 * @param typeParams A list of type arguments.
 * @return An ObjectCreatorAsConcreteType<T> object to allow newInstance() on generic Bond structure.
 */
fun<T> build(genericClass : Class<T>, typeParams: Array<ObjectCreatorAsAny>) : ObjectCreatorAsConcreteType<T> {
    if (!genericClass.isBondGeneratedStruct()) {
        throw UnsupportedBondTypeException(genericClass)
    }

    if (!genericClass.isGenericClass()) {
        throw UnsupportedBondTypeException(genericClass)
    }

    return GenericTypeObjectCreator(genericClass, typeParams)
}

/**
 * A helper function to create an ObjectCreator instance for non-generic Kotlin type.
 * @param objectClass Given Kotlin generic class.
 * @param typeParams A list of type arguments.
 * @return An ObjectCreatorAsConcreteType<T> object to allow calling newInstance().
 */
fun<T: Any> build(objectClass : KClass<T>, typeParams: Array<ObjectCreatorAsAny>) : ObjectCreatorAsConcreteType<T> {
    val jClass = objectClass.java
    return build(jClass, typeParams)
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toJCreators(concreteTypeParameters : List<Class<*>>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it) }.toTypedArray()
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toJCreators(concreteTypeParameters : Array<Class<*>>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it) }.toTypedArray()
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toJCreatorsV(vararg concreteTypeParameters : Class<*>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it) }.toTypedArray()
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toKCreators(concreteTypeParameters : List<KClass<*>>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it.java) }.toTypedArray()
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toKCreators(concreteTypeParameters : Array<KClass<*>>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it.java) }.toTypedArray()
}

/**
 * A helper function to convert list of type parameters from Class<*> to ObjectCreator.
 * @param concreteTypeParameters Generic type arguments as a list of Class<*>.
 * @return Generic type arguments as a list of ObjectCreatorAsAny objects.
 */
fun toKCreatorsV(vararg concreteTypeParameters : KClass<*>) : Array<ObjectCreatorAsAny> {
    return concreteTypeParameters.map { UnknownClassObjectCreator(it.java) }.toTypedArray()
}