import com.google.common.collect.Sets
import groovy.util.logging.Slf4j
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import groovy.util.slurpersupport.NodeChildren
import groovy.xml.StreamingMarkupBuilder

/**
 *
 */
@Slf4j
public class FindBugsDiff {

    /**
     *
     */
    public static class Result {
        Writable xmlNew;
        Writable xmlFixed;
        Writable xmlSame;
    }

    /**
     *
     * @param fromFilePath
     * @param toFilePath
     * @return
     */
    public static Result diffFromFilePaths(String fromFilePath, String toFilePath) {
        File fromFile = new File(fromFilePath)
        File toFile = new File(toFilePath)

        return diff(fromFile, toFile)
    }

    /**
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public static Result diff(File fromFile, File toFile) {
        String fromText = fromFile.getText()
        String toText = toFile.getText()

        return diff(fromText, toText)
    }

    /**
     *
     * @param fromText
     * @param toText
     * @return
     */
    public static Result diff(String fromText, String toText) {

        GPathResult bugCollectionFrom = new XmlSlurper().parseText(fromText)
        NodeChildren bugInstancesFrom = bugCollectionFrom.BugInstance

        GPathResult bugCollectionTo = new XmlSlurper().parseText(toText)
        NodeChildren bugInstancesTo = bugCollectionTo.BugInstance

        Map<String, NodeChild> mapFrom = makeBugMap(bugInstancesFrom)
        Map<String, NodeChild> mapTo = makeBugMap(bugInstancesTo)

        Set keysFrom = mapFrom.keySet()
        Set keysTo = mapTo.keySet()

        Set bugsFixed = Sets.difference(keysFrom, keysTo)
        Set bugsSame = Sets.intersection(keysFrom, keysTo)
        Set bugsNew = Sets.difference(keysTo, keysFrom)

        Map<String, NodeChild> bugsMapFixed = mapFrom.findAll { it.key in bugsFixed }
        Map<String, NodeChild> bugsMapSame = mapTo.findAll { it.key in bugsSame }
        Map<String, NodeChild> bugsMapNew = mapTo.findAll { it.key in bugsNew }

        Writable xmlFixed = toReportXml(bugCollectionFrom, bugsMapFixed)
        Writable xmlSame = toReportXml(bugCollectionTo, bugsMapSame)
        Writable xmlNew = toReportXml(bugCollectionTo, bugsMapNew)

        Result result = new Result(
                xmlNew: xmlNew,
                xmlFixed: xmlFixed,
                xmlSame: xmlSame
        )

        return result
    }

    /**
     *
     * @param bugCollection
     * @param bugsMap
     * @return
     */
    public static Writable toReportXml(GPathResult bugCollection, Map<String, NodeChild> bugsMap) {

        Set<String> bugsCategories = bugsMap.collect {
            return it.value.@category.toString()
        }
        Set<String> bugsCodes = bugsMap.collect {
            return it.value.@abbrev.toString()
        }

        Writable newXml = (Writable) new StreamingMarkupBuilder().bind {
            it.BugCollection {
                // the summary stats aren't diff-aware
//                mkp.yield bugCollection.FindBugsSummary

                mkp.yield bugCollection.Project
                mkp.yield bugsMap.values()
                mkp.yield bugCollection.BugCategory.findAll {
                    return bugsCategories.contains(it.@category.toString())
                }
                mkp.yield bugCollection.BugPattern.findAll {
                    return bugsCategories.contains(it.@category.toString())
                }
                mkp.yield bugCollection.BugCode.findAll {
                    return bugsCodes.contains(it.@abbrev.toString())
                }
            }
        }

        return newXml
    }

    /**
     *
     * @param bugInstances
     * @return
     */
    public static Map<String, NodeChild> makeBugMap(NodeChildren bugInstances) {
        Map<String, NodeChild> map = [:]
        bugInstances.each() { NodeChild bugInstance ->
            map.put(bugInstance.@instanceHash.toString(), bugInstance)
        }
        return map
    }

}
