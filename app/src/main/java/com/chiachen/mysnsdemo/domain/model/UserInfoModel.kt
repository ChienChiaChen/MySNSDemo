package com.chiachen.mysnsdemo.domain.model

import android.net.Uri

data class UserInfoModel(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: Uri?,
    val isEmailVerified: Boolean
)