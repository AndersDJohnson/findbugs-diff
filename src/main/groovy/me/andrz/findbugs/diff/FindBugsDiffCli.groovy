package me.andrz.findbugs.diff

import groovy.util.logging.Slf4j

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
        URL xslURL = FindBugsDiffCli.class.getResource('/diff.xsl')
    }

    public static void main(String[] args) {
        def cli = new CliBuilder(usage:'from-file to-file')

        cli.h(longOpt: 'help', 'print this message')

        cli.outDir(args: 1, '')

        cli.outPathFixed(args: 1, '')
        cli.outPathSame(args: 1, '')
        cli.outPathNew(args: 1, '')

        // TODO: Add option to override XSL file used.
        // TODO: Add option to override HTML output.

        OptionAccessor options = cli.parse(args)
        assert options // would be null (false) on failure
        def optArgs = options.arguments()

        if ( ! optArgs || options.help ) {
            cli.usage()
            return
        }

        log.debug "options:"
        if (log.isDebugEnabled()) {
            cli.options.options.findAll({ options[it.key] }).each {
                log.debug "${it.key}: ${options[it.key]}"
            }
        }
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

        if (options.outDir) {
            findBugsDiffOptions.xmlFixedPath = options.outDir + '/' + findBugsDiffOptions.xmlFixedPath
            findBugsDiffOptions.xmlSamePath = options.outDir + '/' + findBugsDiffOptions.xmlSamePath
            findBugsDiffOptions.xmlNewPath = options.outDir + '/' + findBugsDiffOptions.xmlNewPath
        }

        if (options.outPathFixed) findBugsDiffOptions.xmlFixedPath = options.outPathFixed
        if (options.outPathSame) findBugsDiffOptions.xmlFixedPath = options.outPathSame
        if (options.outPathNew) findBugsDiffOptions.xmlFixedPath = options.outPathNew

        FindBugsDiffIo.run(findBugsDiffOptions)
    }
}
