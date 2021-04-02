package com.okugata.githubuser.model

import android.os.Parcelable
import com.okugata.githubuser.util.getGithubAPI
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    var username: String="",
    var name: String="Private Name",
    var location: String="No Location",
    var repository: Int=0,
    var company: String="No Company",
    var followers: Int=0,
    var following: Int=0,
    var avatar: Int=0,
    var avatarUrl: String="",
    var isGetAPI: Boolean=true
) : Parcelable {
    init {
        if (isGetAPI) {
            getGithubAPI("https://api.github.com/users/$username"){ error, response ->
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
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}