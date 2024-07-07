package com.notifgram.core.domain.entity

sealed class UseCaseException(cause: Throwable) : Throwable(cause) {

    class ChannelException(cause: Throwable) : UseCaseException(cause)
    class PostException(cause: Throwable) : UseCaseException(cause)
    class ChannelNotFoundException(cause: Throwable = Throwable("The Channel is not found")) :
        UseCaseException(cause)

    class UnknownException(cause: Throwable) : UseCaseException(cause)

    companion object {

        fun createFromThrowable(throwable: Throwable): UseCaseException {
            return if (throwable is UseCaseException) throwable else UnknownException(throwable)
        }
    }
}