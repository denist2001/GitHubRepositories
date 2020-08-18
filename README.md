# Task description

* Create an Android app that queries a web service and displays the results
* Minimum API level - 24, target API level - latest
* Project language - Kotlin
* Focus on quality over quantity e.g.
    ** Clean code, readability, consistency
    ** UI performance
    ** Material UI guidelines
* Must to be used:
    ** Retrofit
    ** coroutines/RxJava or both
    ** Databinding
    ** androidX: LiveData, ViewModel, Navigation
    ** styling technics (themes + styles)
* Feel free to use any other tools, libraries, technics and patterns you like to work with
* Make sure the project runs out of the box on our side
* Make sure the app runs both in portrait and landscape mode and state is preserved when configuration is changed

# Mandatory stage

* Create an Activity where the user can search for repositories on GitHub)
* Display the list of repositories
* Each item should contain
    ** The avatar image of the owner
    ** The repository name
    ** The description
    ** The number of forks
* API requires authentication

For information on the GitHub API [see](https://docs.github.com/en/rest)

# Optional stage (increases your overall chances)

* Show repository details when clicking an item in the list
* Display
    * The name of the repository
    * The watchers count
    * The list of watchers
* Use androidx.Pagination (v3)
* Use dependency injection (preferably Koin)
* Create unit or/and instrumentation tests