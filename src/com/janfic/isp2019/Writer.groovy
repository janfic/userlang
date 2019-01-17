package com.janfic.isp2019

/**
 *  The transcriber of the Translator. Takes in translations and writes the translation into the current file 
 */
class Writer {
       
    //Tools
    private static File file
    private static PrintStream output
    private static GroovyShell shell
   
    /**
     *   Sets the writer's file. This creates also creates the new output file. *.groovy
     **/
    public static void setFile(File f) {
        file = f
        
        File newFile = new File(f.getParentFile(), "groovy/" + f.getName().replace(".usr",".groovy"))
        newFile.getParentFile().mkdirs()
        newFile.createNewFile()
        output = new PrintStream(newFile)
        shell = new GroovyShell()
    }
    
    /**
     *   Takes any translation map and translate the map to the according type of definition
     **/
    public static void write(Map translation) {
        if(translation.type.equals("Component")) {
            writeComponent(translation)
        }
        else if(translation.type.equals("Asset")) {
            writeAsset(translation)
        }
        else if(translation.type.equals("Entity")) {
            writeEntity(translation)
        }
        else if(translation.type.equals("System")) {
            writeSystem(translation)
        }
    }
    
    /**
     *  Writes a component defintion based on the translation given to it
     */
    public static void writeComponent(Map translation) {
        output.println "package pack.${translation.pack}.components" // package writing
        output.println ""
        output.println "import pack.${translation.pack}.assets.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports 
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name implements Component {" // start of class
        writeFields(translation.fields)
        output.println ""
        output.println "\t$translation.name() {" // constructor
        writeDefaults(translation.defaults)
        output.println "\t}"
        output.print "}" // end class
    }
    
    /**
     *   Writes an Entity Definition based on the translation given to it
     */
    public static void writeEntity(Map translation) {
        output.println "package pack.${translation.pack}.entities"
        output.println ""
        output.println "import pack.${translation.pack}.assets.*"
        output.println "import pack.${translation.pack}.components.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.Entity"
        output.println ""
        output.println "class $translation.name extends Closure<Entity> {"
        output.println "\t@Override"
        output.println "\tEntity call() {"
        output.println "\t\tEntity entity = new Entity()"
        writeComponents(translation.components, translation.defaults)
        output.println "\t\treturn entity"
        output.println "\t}"
        output.println ""
        output.println "\t$translation.name() {"
        output.println "\t\tsuper(null)"
        output.println "\t}"
        output.println "}"
    }
    
    public static void writeComponents(String[] comps, Map defaults = [:]) {
        comps.each({
                output.print "\t\tentity.add(new $it("
                if(defaults) {
                    Map d = defaults[it.toString()]
                    if(d!=null) {
                        writeMap(d)
                    }
                }
                output.println "))"
            })
    }
    
    /**
     *   Writes a System Definition based on the translation given to it
     */
    public static void writeSystem(Map translation) {
        output.println "package pack.${translation.pack}.systems"
        output.println ""
        output.println "import pack.${translation.pack}.components.*"
        output.println "import pack.${translation.pack}.assets.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.*"
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println "import com.badlogic.gdx.graphics.glutil.*" 
        output.println ""
        output.println "class $translation.name extends EntitySytem {"
        for(family in (translation.families as Map)) {
            output.println "\tprivate ImmutableArray<Entity> $family.key"
        }
        output.println ""
        //
        List components = []
        translation.families.each({
                components += it.value.collect({key, value -> value})
            })
        components.unique({a , b -> a <=> b})
        println components
        components.each({output.println "\tprivate ComponentMapper<$it> ${it.toLowerCase()}Mapper = ComponentMapper.getFor(${it}.class)"})
        //
        output.println ""
        writeFields(translation.fields)
        output.println ""
        output.println "\t$translation.name() {"
        writeDefaults(translation.defaults)
        output.println "\t}"
        output.println ""
        output.println "\tvoid addedToEngine(Engine engine) {"
        for(family in (translation.families as Map)) {
            output.println "\t\t$family.key = engine.getFor("
            output.println "\t\t\tFamily.all("
            (family.value as Map).eachWithIndex({ key, value, index ->
                    output.print "\t\t\t\t${value}.class"
                    if(index < family.value.size() - 1) {
                        output.println ","
                    }
                    else {
                        output.println ""
                    }
                })
            output.println "\t\t\t).get()"
            output.println "\t\t)"
        }
        output.println "\t}"
        output.println ""
        output.println "\tvoid update(float deltaTime) {"
        if(translation.eachFrame)
        output.println "\t\t$translation.eachFrame"
        for(family in translation.eachEntity) {
            output.println "\t\tfor( entity in $family.key) {"
            for(c in translation.families."$family.key") {
                output.println "\t\t\t$c.value $c.key = ${c.value.toLowerCase()}Mapper.get(entity)"
            }
            output.println "\t\t\t$family.value"
            output.println "\t\t}"
        }
        if(translation.endFrame)
        output.println "\t\t$translation.endFrame"
        output.println "\t}"
        output.println "}"
    }
    
    /**  
     *  Writes a Body of a System or Asset. Replaces all asset calls with translation 
     */
    public static void writeBody(String body) {
        output.print "\t\t\t"
        def assetCalls = body.findAll("\\[\\s*asset\\s*:\\s*\"[a-zA-Z]+\".*\\]")
        def parts = body.split("\\[\\s*asset\\s*:\\s*\"[a-zA-Z]+\".*\\]")
        if(assetCalls.size() > 0)
        parts.eachWithIndex({ str, index ->
                output.print str
                writeAssetCall((Map)shell.evaluate(assetCalls.get(index)))
            })
        else 
        output.println body
        output.println ""
        //output.println "\t\t\t${body.replaceAll("\\n\\t*","\n\t\t\t")}"
    }
    
    /**
     *   Writes a Pack Definition based on the translation given to it
     */
    public static void writePack(Map translation) {
        
    }
    
    /**
     *   Writes a Script Definition based on the translation given to it
     */
    public static void writeAsset(Map translation) {
        output.println "package pack.${translation.pack}.assets" // writes package
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports 
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name extends Closure {" // start of class
        writeFields(translation.given)
        output.println ""
        output.println "\t@Override" //writes the "run" 
        output.println "\tdef call() {"
        writeBody(translation.body)
        output.println "\t}"
        output.println ""
        output.println "\t${translation.name}() {" // initializer
        output.println "\t\tsuper(null)"
        writeDefaults(translation.defaults)
        output.println "\t}"
        output.println "}"
    }
    
    /**
     *  Writes field/given declarations 
     */
    public static void writeFields(Map fields) {
        fields.each({output.println "\t${it.value.getSimpleName()} $it.key"})
    }
    
    /**
     *   Writes initializsers in the constructor. AKA the defaults of the definition
     */
    public static void writeDefaults(Map defaults) {
        defaults.each({
                output.print "\t\t$it.key = "
                if(it.value instanceof Map) {
                    writeAssetCall(it.value)
                    output.println()
                }
                else {
                    if(it.value instanceof String)
                    output.println "\"$it.value\""
                    else
                    output.println "$it.value"
                }
            })
    }
    
    /**
     *   Writes a Script or Asset call
     */ 
    public static String writeAssetCall(Map call) {
        output.print "new "
        if(call.asset) {
            output.print "$call.asset("
            if(call.size() > 1){
                call = call - [asset:call.asset]
                writeMap(call)
            }
            output.print ")()"
        }
        else if(call.make) {
            output.print "${call.make.getSimpleName()}("
            if(call.size() > 1){
                call = call - [make:call.make]
                writeMap(call)
            }
            output.print ")"
        }
    }
    
    public static void writeMap(Map map) {
        int index = 0
        for( e in map) {
            if(e.key.equals("asset")) {
                writeAssetCall(map)
                break
            }
            else if(e.value instanceof Map) {
                output.print "$e.key:"
                writeMap(e.value)
            }
            else if(e.value instanceof String ){
                output.print "$e.key:\"$e.value\""
            }
            else {
                output.print "$e.key:${e.value}"
            }
            output.print "${index < map.size() - 1 ? " , " : ""}"
            index++
        }
    }
}