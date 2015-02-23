import groovy.util.logging.Slf4j
import groovy.xml.XmlUtil

/**
 *
 */
@Slf4j
class FindBugsDiffIo {

    /**
     *
     * @param options
     */
    public static void run(FindBugsDiffCli.FindBugsDiffOptions options) {
        FindBugsDiff.Result result = FindBugsDiff.diffFromFilePaths(options.pathFrom, options.pathTo)

        generateHtml(result.xmlFixed, options.xmlFixedPath, options.xslPath)
        generateHtml(result.xmlSame, options.xmlSamePath, options.xslPath)
        generateHtml(result.xmlNew, options.xmlNewPath, options.xslPath)
    }

    /**
     *
     * @param xmlPath
     * @param xml
     * @param xslPath
     */
    public static void generateHtml(Writable xml, String xmlPath, String xslPath) {
        writeXml(xml, xmlPath)
        String htmlOutPath = xmlPath.replaceAll(/xml$/, 'html')
        antXmlToHtml(xmlPath, htmlOutPath, xslPath)
    }

    /**
     *
     * @param xml
     * @param xmlPath
     */
    public static void writeXml(Writable xml, String xmlPath) {
        (new File(xmlPath)).createNewFile()
        XmlUtil.serialize(xml, new FileWriter(xmlPath))
    }

    /**
     *
     * @param inXmlPath
     * @param outXmlPath
     * @param xslPath E.g. 'C:/dev/apps/findbugs/findbugs-3.0.0/src/xsl/default.xsl'
     */
    public static void antXmlToHtml(String inXmlPath, String outHtmlPath, String xslPath) {
        def ant = new AntBuilder()

        ant.xslt(
                in: inXmlPath,
                out: outHtmlPath,
                style: xslPath
        )
    }

}
