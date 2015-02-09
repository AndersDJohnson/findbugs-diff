import groovy.util.logging.Slf4j;

/**
 *
 */
@Slf4j
public class FindBugsDiffCli {

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

        String fromFilePath = optArgs[0]
        String toFilePath = optArgs[1]

        def diff = FindBugsDiff.diff(fromFilePath, toFilePath)

        println diff
    }
}
