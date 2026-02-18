package hudson.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.MockFolder;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class RunParameterValueIntegrationTest {

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    @Test
    void existingRunAccepted(JenkinsRule jenkinsRule) throws Exception {
        final MockFolder folder = jenkinsRule.createFolder("folder");
        final FreeStyleProject job = folder.createProject(FreeStyleProject.class, "job");
        job.updateNextBuildNumber(57);
        jenkinsRule.assertBuildStatusSuccess(job.scheduleBuild2(0));

        RunParameterValue rpv = new RunParameterValue("whatever", "folder/job#57");
        assertEquals("whatever", rpv.getName());
        assertEquals("folder/job", rpv.getJobName());
        assertEquals("57", rpv.getNumber());
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    @Test
    void nonexistentRunRejected(JenkinsRule jenkinsRule) throws Exception {
        // A run referencing a non-existent job should be rejected
        assertThrows(IllegalArgumentException.class, () -> new RunParameterValue("whatever", "nonexistent/job#1"));
    }
}
