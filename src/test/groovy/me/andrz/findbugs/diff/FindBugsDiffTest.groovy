package me.andrz.findbugs.diff

import groovy.util.logging.Slf4j
import org.junit.Test

/**
 *
 */
@Slf4j
class FindBugsDiffTest {

    private static final String classPathFixtures = '/me/andrz/findbugs/diff/fixtures'
    private static final URL case1URLFrom = FindBugsDiffTest.class.getResource(classPathFixtures + '/1/from/findbugs.xml')
    private static final URL case1URLTo = FindBugsDiffTest.class.getResource(classPathFixtures + '/1/to/findbugs.xml');

    @Test
    public void testRun() {

        File cwd = new File('.')
        log.debug(cwd.canonicalPath)

        String pathFrom = case1URLFrom.file
        String pathTo = case1URLTo.file

        log.debug(pathFrom)
        log.debug(pathTo)

        FindBugsDiffCli.FindBugsDiffOptions findBugsDiffOptions = new FindBugsDiffCli.FindBugsDiffOptions(
                pathFrom: pathFrom,
                pathTo: pathTo
        )

        FindBugsDiffIo.run(findBugsDiffOptions)
    }

    public static void main(String[] args) {
        String pathFrom = "C:/Users/Anders/code/findbugs-example/findbugs-example-1/target/findbugsXml.xml";
        String pathTo = "C:/Users/Anders/code/findbugs-example/findbugs-example-2/target/findbugsXml.xml";

        FindBugsDiffCli.FindBugsDiffOptions findBugsDiffOptions = new FindBugsDiffCli.FindBugsDiffOptions(
                pathFrom: pathFrom,
                pathTo: pathTo
        )

        FindBugsDiffIo.run(findBugsDiffOptions)

        // for comparison
        FindBugsDiffIo.xmlToHtml(pathTo, 'findbugs-1-default.html',
                new File('C:\\dev\\apps\\findbugs\\findbugs-3.0.0\\lib\\findbugs\\default.xsl').toURI().toURL())
        FindBugsDiffIo.xmlToHtml(pathTo, 'findbugs-1-fancy.html',
                new File('C:\\dev\\apps\\findbugs\\findbugs-3.0.0\\lib\\findbugs\\fancy.xsl').toURI().toURL())

    }

}
