package com.mbanna.bookshelf.utils

object   Constants {

    const val STATUS_OK = 200
    const val STATUS_INVALID = 504
    const val USER_ALREADY_EXIST = 111

    const val SORT_BY_FAVOURITE = 1
    const val SORT_BY_TITLE = 2
    const val SORT_BY_HITS = 3


    // Password Errors

    const val ERROR_SMALL_PASSWORD = "Password must be at least 8 characters long."
    const val ERROR_CONTAIN_NUM = "Password must contain at least one number."
    const val ERROR_CONTAIN_SPECIAL_CHAR = "Password must contain at least one special character (!@#\$%&())."
    const val ERROR_CONTAIN_LOWERCASE = "Password must contain at least one lowercase letter."
    const val ERROR_CONTAIN_UPPERCASE = "Password must contain at least one uppercase letter."

    object TextValid{
        const val EMPTY_FIELD = -1
    }
}