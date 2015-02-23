import groovy.util.logging.Slf4j;

/**
 *
 */
@Slf4j
public class FindBugsDiffCli {

    public static class FindBugsDiffOptions {
        String pathFrom
        String pathTo
        String xmlFixedPath = 'findbugs-fixed.xml'
        String xmlSamePath = 'findbugs-same.xml'
        String xmlNewPath = 'findbugs-new.xml'
        String xslPath = (new File(FindBugsDiffCli.class.getResource('/diff.xsl').toURI())).absolutePath
    }

    public static void main(String[] args) {
        def cli = new CliBuilder(usage:'from-file to-file')
        OptionAccessor options = cli.parse(args)
        assert options // would be null (false) on failure
        def optArgs = options.arguments()

        log.debug "options: $options"
        log.debug "optArgs: $optArgs"

        handleFileOpts(options, optArgs)
    }

    private static void handleFileOpts(OptionAccessor options, List<String> optArgs) {
        log.info "Handling file options..."

        String pathFrom = optArgs[0]
        String pathTo = optArgs[1]

        FindBugsDiffOptions findBugsDiffOptions = new FindBugsDiffOptions(
                pathFrom: pathFrom,
                pathTo: pathTo
        )

        FindBugsDiffIo.run(findBugsDiffOptions)
    }
}
