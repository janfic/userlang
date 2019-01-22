package com.janfic.isp2019

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

/*
 *   Translates files of USER into Map objects that contain a translation into Groovy
 */
class Translator {
    
    //Tools
    private static File file
    private static Binding binding
    private static GroovyShell shell
    private static GroovyClassLoader loader
    private static CompilerConfiguration config
    private static ImportCustomizer imports
    
    //Translated Items
    private static Map translation = [name:"", type:"", pack:""]
    
    //Static initialization of Tools
    static {
        config = new CompilerConfiguration()
        imports = new ImportCustomizer()
        imports.addStarImports("com.badlogic.gdx.graphics.g2d",
                                "com.badlogic.gdx.files", 
                                "com.badlogic.gdx.math",
                                "com.badlogic.gdx.graphics",
                                "com.badlogic.gdx.graphics.glutils",
                                "com.badlogic.gdx"
        )
        binding = new Binding()
        config.addCompilationCustomizers(imports)
    }
    
    /**
     *   Set the current USER file to be translated. Also resets the translation
     */
    public static void setFile( File f ) {
        file = f
        translation = [name:"",type:"", pack:""]
        translation.name = file.name.substring(0, file.name.lastIndexOf('.'))
    }
    
    /**
     *   Compiles the current USER file
     */
    public static void compile() {
        makeBinding()
        loader = new GroovyClassLoader(new GroovyClassLoader(), config) //ugh
        shell = new GroovyShell(loader, binding, config)
        shell.evaluate(file)
    }
    
    /**
     *   Sets up the current Binding, which is the main object in charge of the translation. Syntax, Structure, and Keywords are established here
     */
    public static void makeBinding() {
        
        //creates the 'component' keyword
        binding.component = {
            translation.type = "Component"
        }
        
        //creates the 'asset' keyword
        binding.asset = {
            translation.type = "Asset"
        }
        
        //creats the 'entity' keyword
        binding.entity = {
            translation.type = "Entity"
        }
        
        //creates the 'system' keyword
        binding.system = {
            translation.type = "System"
        }
        
        binding.uses = { String...packs ->
            translation.uses = packs
        }
        
        //creates a valid phrase for the name of the definition in USER
        binding."${translation.name}" = { Closure body -> 
            translation.eachEntity = [:]
            
            // creates the 'fields' keyword and its structure
            body.fields = { Map map ->
                translation.given = translation.fields = map
            }
            
            // makes 'given' have the same effect as 'fields'
            body.given = body.fields 
            
            //creates the 'run' keyword and its following required structure
            body.run = {Closure b -> 
                translation.body = extractScript("(?s)run\\s*\\{.*\\}")
            }
            
            //creates the 'defaults' keyword and its following required structure
            body.defaults = { Map map ->
                translation.defaults = map
            }
            
            //create the 'components' keyword and its following required structure
            body.components = { String... comps ->
                translation.components = comps
            }            
            
            body.family = { Map map ->
                translation.families = [entities:map]
            }
            
            
            //system
            body.families = { Map map ->
                translation.families = map
            }
            
            body.eachEntity =  {Map map ->
                for(f in map)
                translation.eachEntity."$f.key" = extractScript("(?s)eachEntity\\s*$f.key\\s*:\\s*\\{.*\\}").trim()
            }
            
            body.eachFrame = {Closure c ->
                translation.eachFrame = extractScript("(?s)eachFrame\\s*\\{.*\\}").trim()
            }
            
            body.endFrame = {Closure c ->
                translation.endFrame = extractScript("(?s)endFrame\\s*\\{.*\\}").trim()
            }
            
            
            //pack
            body.author = { String author ->
                translation.author = author
            }
            
            body.info = { String info ->
                translation.info = info
            }
            
            body.display = { String display ->
                translation.display = display
            }
            
            body.assets = { String...assets ->
                translation.systems = assets
            }
            
            body.entities = { String...entities ->
                translation.systems = entities
            }
            
            body.systems = { String...systems ->
                translation.systems = systems
            }
            
            body.active = { String...actoveSystems ->
                translation.active = activeSystems
            }
            //runs the body
            body()
            return body
        }
        
        //creates the 'pack' keyword and required input
        binding.pack = { def p ->
            if(p instanceof String) {
                translation.pack = p
            }
            else if(p instanceof Closure ) {
                translation.type = "Pack"
                translation.pack = "${translation.name.toLowerCase()}"
            }
        }
    }
    
    /**
     *   Extracts the runnable script from the file by extracting it from the String version of the file.
     */
    public static String extractScript(String regex) {
        String contents = file.text
        String r = contents.find(regex)
        r = r.substring(r.indexOf("{") + 1, r.lastIndexOf("}") - 1)
        int index = 0
        int count = 0
        for(int i = 0; i<r.length(); i++) {
            if(r.charAt(i) == '{') {
                count++
            }
            else if(r.charAt(i) == '}') {
                count--
            }
            if(count<0) {
                break
            }
            index = i
        }
        r.substring(0,index)
    }
    
    /**
     *   Returns the translation
     */
    public static Map getTranslation() {
        return translation
    }
}