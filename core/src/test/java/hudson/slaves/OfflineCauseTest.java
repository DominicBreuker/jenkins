package hudson.slaves;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

class OfflineCauseTest {

    @Test
    void testChannelTermination_NoStacktrace() {
        String exceptionMessage = "exception message";
        OfflineCause.ChannelTermination cause = new OfflineCause.ChannelTermination(new RuntimeException(exceptionMessage));
        assertThat(cause.toString(), not(containsString(exceptionMessage)));
    }

    @Test
    void userCauseEscapesHtmlInToString() {
        OfflineCause.UserCause cause = new OfflineCause.UserCause(null, "<img src=x onerror=alert(1)>");
        String result = cause.toString();
        assertThat("HTML should be escaped in toString()", result, not(containsString("<img")));
        assertThat("HTML entities should be present", result, containsString("&lt;img"));
    }

    @Test
    void userCauseEscapesHtmlInGetMessage() {
        OfflineCause.UserCause cause = new OfflineCause.UserCause(null, "<script>alert('xss')</script>");
        String message = cause.getMessage();
        assertThat("HTML should be escaped in getMessage()", message, not(containsString("<script>")));
        assertThat("HTML entities should be present", message, containsString("&lt;script&gt;"));
    }

    @Test
    void userCauseHandlesNullMessage() {
        OfflineCause.UserCause cause = new OfflineCause.UserCause(null, null);
        // Should not throw
        cause.toString();
        cause.getMessage();
    }

}
