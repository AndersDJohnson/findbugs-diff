import groovy.util.logging.Slf4j
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

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

        generateHtml(result.xmlFixed, options.xmlFixedPath, options.xslURL)
        generateHtml(result.xmlSame, options.xmlSamePath, options.xslURL)
        generateHtml(result.xmlNew, options.xmlNewPath, options.xslURL)
    }

    /**
     *
     * @param xmlPath
     * @param xml
     * @param xslUrl
     */
    public static void generateHtml(Writable xml, String outXmlPath, URL xslUrl) {
        writeXml(xml, outXmlPath)
        String htmlOutPath = outXmlPath.replaceAll(/xml$/, 'html')
        xmlToHtml(outXmlPath, htmlOutPath, xslUrl)
    }

    /**
     *
     * @param xml
     * @param xmlPath
     */
    public static void writeXml(Writable xml, String outXmlPath) {
        File outXmlFile = new File(outXmlPath)
        outXmlFile.parentFile?.mkdirs()
        outXmlFile.createNewFile()

        XmlUtil.serialize(xml, new FileWriter(outXmlFile))
    }

    /**
     *
     * @param inXmlPath
     * @param outHtmlPath
     * @param xslUrl E.g. 'file:///C:/dev/apps/findbugs/findbugs-3.0.0/src/xsl/default.xsl'
     */
    public static void xmlToHtml(String inXmlPath, String outHtmlPath, URL xslUrl) {

        InputStream xslStream = xslUrl.openStream()
        InputStreamReader xslReader = new InputStreamReader(xslStream)

        FileReader inXmlReader = new FileReader(new File(inXmlPath))

        File outHtmlFile = new File(outHtmlPath)
        outHtmlFile.parentFile?.mkdirs()
        outHtmlFile.createNewFile()

        OutputStream outHtmlStream = new FileOutputStream(outHtmlFile)

        def factory = TransformerFactory.newInstance()
        def transformer = factory.newTransformer(new StreamSource(xslReader))
        transformer.transform(new StreamSource(inXmlReader), new StreamResult(outHtmlStream))

    }

}
