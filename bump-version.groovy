#!/usr/bin/env groovy

if (args.length<2) {
    println "Usage :"
    println "./bump-version.groovy oldVersion newVersion"
    return
}

def oldVersion = args[0]
def newVersion = args[1]

println "Handling all pom.xml|plugin.xml files..."
int nbHandled = 0
new File(".").eachFileRecurse { File f ->
    if (!f.directory && !f.absolutePath.contains("target") && (f.name == "pom.xml" || f.name == "plugin.xml")) {
        println "replacing in $f.absolutePath..."
        // replace version in file
        String text = f.text
        int nbReplaces = 0
        while (text.contains(oldVersion)) {
            text = text.replaceFirst(oldVersion,newVersion)
            nbReplaces++
        }
        assert "invalid number of replacements in $f.absolutePath", nbReplaces == 1
        f.text = text
        println "... done with $f.absolutePath"
        nbHandled++
    }
}

assert nbHandled == 7
println "Replaced versions in $nbHandled files"

println "Done. Version bumped from $oldVersion to $newVersion"
