import com.google.common.collect.Sets
import groovy.util.slurpersupport.NodeChild
import groovy.util.slurpersupport.NodeChildren
import groovy.xml.XmlUtil

/**
 *
 */
public class FindBugsDiff {

    public static void main(String[] args) {
        String fromFilePath = "C:/Users/Anders/code/findbugs-example/findbugs-example-1/target/findbugsXml.xml";
        String toFilePath = "C:/Users/Anders/code/findbugs-example/findbugs-example-2/target/findbugsXml.xml";

        diffFromFilePaths(fromFilePath, toFilePath)
    }

    public static String diffFromFilePaths(String fromFilePath, String toFilePath) {
        File fromFile = new File(fromFilePath)
        File toFile = new File(toFilePath)

        return diff(fromFile, toFile)
    }

    public static String diff(File fromFile, File toFile) {
        String fromText = fromFile.getText()
        String toText = toFile.getText()

        return diff(fromText, toText)
    }

    public static String diff(String fromText, String toText) {

        NodeChild fromBugCollection = new XmlSlurper().parseText(fromText)
        NodeChildren fromBugInstances = fromBugCollection.BugInstance

        NodeChild toBugCollection = new XmlSlurper().parseText(toText)
        NodeChildren toBugInstances = toBugCollection.BugInstance

        Map<String, NodeChild> fromMap = makeBugMap(fromBugInstances)
        Map<String, NodeChild> toMap = makeBugMap(toBugInstances)

        Set fromKeys = fromMap.keySet()
        Set toKeys = toMap.keySet()

        Set bugsNew = Sets.difference(toKeys, fromKeys)
        Set bugsFixed = Sets.difference(fromKeys, toKeys)

        println 'new: ' + bugsNew
        println '\n'

        bugsNew.each() { hash ->
            println XmlUtil.serialize(toMap[hash])
        }

        println '\n\n'

        println 'fixed: ' + bugsFixed
        println '\n'

        bugsFixed.each() { hash ->
            println XmlUtil.serialize(fromMap[hash])
        }

//        NamespaceAwareHashMap attrs = [:]
//        NodeChild out = new NodeChild(new Node(null, "BugCollection", attrs, null, null), null, null)
//        NodeChild out = fromBugCollection
//        NodeChild out = toBugCollection
//        out.findAll() { node ->
//            node.name == 'BugInstance'
//        }.replaceNode() {}
//        toBugInstances.replaceNode() {}

//        toMap.each() { hash, node ->
//            out.appendNode(node)
//        }

//        println "toSet: $toSet"
//        println "fromSet: $fromSet"
//
//        Set<String> fromDiff = Sets.difference(fromSet, toSet)
//        Set<String> toDiff = Sets.difference(toSet, fromSet)
//
//        println "fromDiff: $fromDiff"
//        println "toDiff: $toDiff"


//        println XmlUtil.serialize(out, new FileWriter("temp-findbugs.xml"))
//        antXmlToHtml()


        return ''
    }

    public static Map<String, NodeChild> makeBugMap(NodeChildren bugInstances) {
        Map<String, NodeChild> map = [:]
        bugInstances.each() { NodeChild bugInstance ->
            map.put(bugInstance.@instanceHash.toString(), bugInstance)
        }
        return map
    }

//    public static void antXmlToHtml() {
//        def ant = new AntBuilder()
//
//        ant.xslt(
//            in: 'temp-findbugs.xml',
//            out: 'temp-findbugs.html',
//            style: 'C:/dev/apps/findbugs/findbugs-3.0.0/src/xsl/default.xsl'
//        )
//    }

}
