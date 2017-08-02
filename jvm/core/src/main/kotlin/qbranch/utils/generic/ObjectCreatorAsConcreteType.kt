package qbranch.utils.generic

interface ObjectCreatorAsConcreteType<T> : ObjectCreatorAsAny {
    fun newInstance() : T
}