package cl.mariofinale;
import org.bukkit.plugin.java.JavaPlugin;
public class villagerSaver extends JavaPlugin{
    @Override
    public void onEnable(){
        PluginPrintln("Registering listener.");
        getServer().getPluginManager().registerEvents(new tListener(), this);
        PluginPrintln("Listener registered.");
        PluginPrintln("Plugin loaded!.");
    }
    @Override
    public void onDisable(){
    //not necessary
    }

    public void PluginPrintln(String line){
        String name = getDescription().getName();
        System.out.println("[" + name + "] " + line );
    }
}
