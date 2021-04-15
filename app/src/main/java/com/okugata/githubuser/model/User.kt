package com.okugata.githubuser.model

import android.os.Parcelable
import com.okugata.githubuser.database.UserFavorite
import com.okugata.githubuser.util.getGithubAPI
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    var username: String = "",
    var name: String = "",
    var location: String = "No Location",
    var repository: Int = 0,
    var company: String = "No Company",
    var followers: Int = 0,
    var following: Int = 0,
    var avatar: Int = 0,
    var avatarUrl: String = "",
    var isGetAPI: Boolean = true,
    var id: Long? = null
) : Parcelable {
    companion object {
        private fun fromUserFavorite(userFavorite: UserFavorite): User {
            return userFavorite.run {
                User(
                    username,
                    name,
                    location,
                    repository,
                    company,
                    followers,
                    following,
                    avatar,
                    avatarUrl,
                    isGetAPI
                )
            }
        }

        fun fromUserFavoriteList(userFavorites: List<UserFavorite>): ArrayList<User> {
            val users = ArrayList<User>()
            for (userFavorite in userFavorites) {
                users.add(fromUserFavorite(userFavorite))
            }
            return users
        }

        fun toUserFavorite(user: User): UserFavorite {
            return user.run {
                UserFavorite(
                    id ?: 0,
                    username,
                    name,
                    location,
                    repository,
                    company,
                    followers,
                    following,
                    avatar,
                    avatarUrl,
                    isGetAPI
                )
            }
        }
    }


    fun update(callback: () -> Unit) {
        if (isGetAPI) {
            getGithubAPI("https://api.github.com/users/$username") { error, response ->
                if (error != null) {
                    return@getGithubAPI
                }
                try {
                    val responseObject = JSONObject(response)
                    name = if (!responseObject.getString("name").equals("null"))
                        responseObject.getString("name") else name
                    location = if (!responseObject.getString("location").equals("null"))
                        responseObject.getString("location") else location
                    company = if (!responseObject.getString("company").equals("null"))
                        responseObject.getString("company") else company

                    repository = responseObject.getInt("public_repos")
                    followers = responseObject.getInt("followers")
                    following = responseObject.getInt("following")
                    avatarUrl = responseObject.getString("avatar_url")
                    isGetAPI = false
                    callback()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}