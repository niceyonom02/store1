import net.jitse.npclib.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EasyStore extends JavaPlugin {
    public static String prefix = ChatColor.GRAY + "[" + ChatColor.YELLOW + "N" + ChatColor.GRAY + "] ";
    public static EasyStore EasyStore;
    private CustomEconomy customEconomy;
    //private NPCLib library;
    private StoreManager storeManager;

    @Override
    public void onEnable(){
        //this.library = new NPCLib(this);
        EasyStore = this;
        registerConfig();
        customEconomy = (CustomEconomy) Bukkit.getPluginManager().getPlugin("customeconomy");
        storeManager = new StoreManager();
        getCommand("상점").setExecutor(storeManager);


    }

    private void registerConfig(){
        File f = new File(getDataFolder(), File.separator + "config.yml");
        if(!f.exists()) {
            saveResource("config.yml", false);
        }
    }

    public CustomEconomy getEconomy() {
        return customEconomy;
    }

    @Override
    public void onDisable(){
        storeManager.save();
    }

    public void getNPCLib() {
       // return library;
    }
}
