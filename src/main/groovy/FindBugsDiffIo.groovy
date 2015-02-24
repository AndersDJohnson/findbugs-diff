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
    public static void generateHtml(Writable xml, String xmlPath, URL xslUrl) {
        writeXml(xml, xmlPath)
        String htmlOutPath = xmlPath.replaceAll(/xml$/, 'html')
        xmlToHtml(xmlPath, htmlOutPath, xslUrl)
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
     * @param outHtmlPath
     * @param xslUrl E.g. 'file:///C:/dev/apps/findbugs/findbugs-3.0.0/src/xsl/default.xsl'
     */
    public static void xmlToHtml(String inXmlPath, String outHtmlPath, URL xslUrl) {

        InputStream xslStream = xslUrl.openStream();
        InputStreamReader xslReader = new InputStreamReader(xslStream)

        FileReader inXmlReader = new FileReader(new File(inXmlPath))

        OutputStream outHtmlStream = new FileOutputStream(outHtmlPath)

        def factory = TransformerFactory.newInstance()
        def transformer = factory.newTransformer(new StreamSource(xslReader))
        transformer.transform(new StreamSource(inXmlReader), new StreamResult(outHtmlStream))

    }

}
