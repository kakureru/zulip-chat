package com.example.courcework

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.chat.ChatFragment
import com.example.courcework.screen.ChatScreen
import com.example.courcework.screen.MessageActionDialog
import com.example.courcework.screen.ReactionDialog
import com.example.courcework.util.MockRequestDispatcher
import com.example.courcework.util.ReactionMockRequestDispatcher
import com.example.courcework.util.loadFromAssets
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatTest : TestCase() {

    @get:Rule
    val mockServer = MockWebServer()

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Context>().getAppComponent().apply {
            apiUrlProvider().apiUrl = mockServer.url("/").toString()
            authHelper().setUserId(604450)
        }
    }

    @After
    fun tearDown() {
        ApplicationProvider.getApplicationContext<Context>().getAppComponent().apply {
            authHelper().cleanAuthData()
        }
    }

    @Test
    fun displayingListOfMessages() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("messages_list.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Отображается список сообщений") {
            chatScreen.messageList.isVisible()
        }
    }

    @Test
    fun gettingEmptyMessageList() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("empty_message_list.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Пустой список сообщений") {
            chatScreen.messageList.hasSize(0)
        }
    }

    @Test
    fun errorLoadingMessages() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody("") }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Отображается сообщение об ошибке") {
            chatScreen.errorMessage.isVisible()
        }
    }

    @Test
    fun groupingMessagesByDates() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("messages_list.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Сообщения сгруппированы по датам") {
            chatScreen.messageList.hasSize(12)
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(9) {
                date.hasText("30 April")
            }
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(0) {
                date.hasText("29 April")
            }
        }
    }

    @Test
    fun messageWithoutReactions() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("message_no_reactions.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Сообщение без реакций") {
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(1) {
                reactions.view.check(matches(hasChildCount(1)))
            }
        }
    }

    @Test
    fun ownReaction() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("message_own_reaction.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Есть собственная реакция к сообщению") {
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(1) {
                onView(nthChildOf(withId(R.id.reactions), 0)).check(matches(ViewMatchers.isSelected()))
            }
        }
    }

    @Test
    fun otherUsersReactions() = run {
        mockServer.dispatcher = MockRequestDispatcher().apply {
            returnsForPath("/messages") { setBody(loadFromAssets("message_other_reactions.json")) }
            returnsForPath("/register") { setBody(loadFromAssets("register_response.json")) }
            returnsForPath("/events") { setBody(loadFromAssets("empty_event_list.json")) }
        }
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()

        step("Есть реакции сторонних пользователей") {
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(1) {
                onView(nthChildOf(withId(R.id.reactions), 0)).check(matches(Matchers.not(ViewMatchers.isSelected())))
                onView(nthChildOf(withId(R.id.reactions), 1)).check(matches(Matchers.not(ViewMatchers.isSelected())))
            }
        }
    }

    @Test
    fun addingReactionWithLongClick() = run {
        val dispatcher = ReactionMockRequestDispatcher(
            messagesRequest = "/messages",
            registerRequest = "/register",
            eventsRequest = "/events",
            messageBaseResponse = MockResponse().setBody(loadFromAssets("message_no_reactions.json")),
            messageWithReactionResponse = MockResponse().setBody(loadFromAssets("message_own_reaction.json")),
            registerResponse = MockResponse().setBody(loadFromAssets("register_response.json")),
            reactionEventResponse = MockResponse().setBody(loadFromAssets("reaction_event.json")),
        )
        mockServer.dispatcher = dispatcher
        val arguments = bundleOf(ChatFragment.ARG_TOPIC_ID to TopicId(0, "Test"))

        launchFragmentInContainer<ChatFragment>(fragmentArgs = arguments, themeResId = R.style.Theme_CourceWork)
        val chatScreen = ChatScreen()
        val actionDialog = MessageActionDialog()
        val reactionDialog = ReactionDialog()

        step("Добавляем реакцию") {
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(1) { longClick() }
            actionDialog.addReaction.click()
            reactionDialog.listReactions.childAt<ReactionDialog.ReactionItem>(0) { click() }
            dispatcher.addReaction()
        }
        step("Реакция появилась") {
            chatScreen.messageList.childAt<ChatScreen.MessageItem>(1) {
                reactions.view.check(matches(hasChildCount(2)))
                onView(nthChildOf(withId(R.id.reactions), 0)).check(matches(ViewMatchers.isSelected()))
            }
        }
    }

    private fun nthChildOf(parentMatcher: Matcher<View?>, childPosition: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            override fun describeTo(description: Description) {
                description.appendText("with $childPosition child view of type parentMatcher")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item?.parent !is ViewGroup)
                    return parentMatcher.matches(item?.parent)
                val group = item.parent as ViewGroup
                return parentMatcher.matches(item.parent) && group.getChildAt(childPosition) == item
            }
        }
    }
}