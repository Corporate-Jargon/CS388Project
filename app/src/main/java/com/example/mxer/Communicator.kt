package com.example.mxer

interface Communicator {
    fun passCommunity(community: Community)
    fun passCompose(community: Community)
    fun passPost(post: Post)
}