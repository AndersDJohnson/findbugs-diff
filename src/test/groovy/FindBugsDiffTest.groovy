import groovy.util.logging.Slf4j

/**
 *
 */
@Slf4j
class FindBugsDiffTest {

    public static void main(String[] args) {
        String pathFrom = "C:/Users/Anders/code/findbugs-example/findbugs-example-1/target/findbugsXml.xml";
        String pathTo = "C:/Users/Anders/code/findbugs-example/findbugs-example-2/target/findbugsXml.xml";

        FindBugsDiffCli.FindBugsDiffOptions findBugsDiffOptions = new FindBugsDiffCli.FindBugsDiffOptions(
                pathFrom: pathFrom,
                pathTo: pathTo
        )

        FindBugsDiffIo.run(findBugsDiffOptions)

        // for comparison
        FindBugsDiffIo.antXmlToHtml(pathTo, 'findbugs-1-default.html',
                'C:\\dev\\apps\\findbugs\\findbugs-3.0.0\\lib\\findbugs\\default.xsl')
        FindBugsDiffIo.antXmlToHtml(pathTo, 'findbugs-1-fancy.html',
                'C:\\dev\\apps\\findbugs\\findbugs-3.0.0\\lib\\findbugs\\fancy.xsl')

    }

}
