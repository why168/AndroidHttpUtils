package io.github.why168.httpcacheandroid.model.github

import com.google.gson.annotations.SerializedName

/**
 *
 * GithubModel
 *
 * @author Edwin.Wu edwin.wu05@gmail.com
 * @version 2018/8/31 下午2:56
 * @since JDK1.8
 */
data class GithubModel(
        @SerializedName("current_user_url") var currentUserUrl: String,
        @SerializedName("current_user_authorizations_html_url") var currentUserAuthorizationsHtmlUrl: String,
        @SerializedName("authorizations_url") var authorizationsUrl: String,
        @SerializedName("code_search_url") var codeSearchUrl: String,
        @SerializedName("commit_search_url") var commitSearchUrl: String,
        @SerializedName("emails_url") var emailsUrl: String,
        @SerializedName("emojis_url") var emojisUrl: String,
        @SerializedName("events_url") var eventsUrl: String,
        @SerializedName("feeds_url") var feedsUrl: String,
        @SerializedName("followers_url") var followersUrl: String,
        @SerializedName("following_url") var followingUrl: String,
        @SerializedName("gists_url") var gistsUrl: String,
        @SerializedName("hub_url") var hubUrl: String,
        @SerializedName("issue_search_url") var issueSearchUrl: String,
        @SerializedName("issues_url") var issuesUrl: String,
        @SerializedName("keys_url") var keysUrl: String,
        @SerializedName("notifications_url") var notificationsUrl: String,
        @SerializedName("organization_repositories_url") var organizationRepositoriesUrl: String,
        @SerializedName("organization_url") var organizationUrl: String,
        @SerializedName("public_gists_url") var publicGistsUrl: String,
        @SerializedName("rate_limit_url") var rateLimitUrl: String,
        @SerializedName("repository_url") var repositoryUrl: String,
        @SerializedName("repository_search_url") var repositorySearchUrl: String,
        @SerializedName("current_user_repositories_url") var currentUserRepositoriesUrl: String,
        @SerializedName("starred_url") var starredUrl: String,
        @SerializedName("starred_gists_url") var starredGistsUrl: String,
        @SerializedName("team_url") var teamUrl: String,
        @SerializedName("user_url") var userUrl: String,
        @SerializedName("user_organizations_url") var userOrganizationsUrl: String,
        @SerializedName("user_repositories_url") var userRepositoriesUrl: String,
        @SerializedName("user_search_url") var userSearchUrl: String){

    override fun toString(): String {
        return "GithubModel(currentUserUrl='$currentUserUrl', currentUserAuthorizationsHtmlUrl='$currentUserAuthorizationsHtmlUrl', authorizationsUrl='$authorizationsUrl', codeSearchUrl='$codeSearchUrl', commitSearchUrl='$commitSearchUrl', emailsUrl='$emailsUrl', emojisUrl='$emojisUrl', eventsUrl='$eventsUrl', feedsUrl='$feedsUrl', followersUrl='$followersUrl', followingUrl='$followingUrl', gistsUrl='$gistsUrl', hubUrl='$hubUrl', issueSearchUrl='$issueSearchUrl', issuesUrl='$issuesUrl', keysUrl='$keysUrl', notificationsUrl='$notificationsUrl', organizationRepositoriesUrl='$organizationRepositoriesUrl', organizationUrl='$organizationUrl', publicGistsUrl='$publicGistsUrl', rateLimitUrl='$rateLimitUrl', repositoryUrl='$repositoryUrl', repositorySearchUrl='$repositorySearchUrl', currentUserRepositoriesUrl='$currentUserRepositoriesUrl', starredUrl='$starredUrl', starredGistsUrl='$starredGistsUrl', teamUrl='$teamUrl', userUrl='$userUrl', userOrganizationsUrl='$userOrganizationsUrl', userRepositoriesUrl='$userRepositoriesUrl', userSearchUrl='$userSearchUrl')"
    }
}