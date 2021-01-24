import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class StoreManager implements CommandExecutor {
    private ArrayList<Category> categories = new ArrayList<>();

    public StoreManager(){
        loadCategories();
   }

   public void save(){
        for(Category category : categories){
            category.saveItems();
        }
   }

   public void loadCategories(){
        if(EasyStore.EasyStore.getConfig().getConfigurationSection("category") != null){
            for(String categoryCode : EasyStore.EasyStore.getConfig().getConfigurationSection("category").getKeys(false)){
                Category category = new Category(categoryCode);
                categories.add(category);
            }
        }
   }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(s.equalsIgnoreCase("상점")){
                if(strings.length > 0){
                    if(player.isOp()){
                        if(strings[0].equalsIgnoreCase("아이템추가")){
                            if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
                                player.sendMessage(EasyStore.prefix + "아이템을 먼저 들어주세요");
                                return false;
                            }

                            if(strings.length > 6){
                                String category = strings[1];
                                String slot = strings[2];
                                String sellPrice = strings[3];
                                String purchasePrice = strings[4];
                                String sellCount = strings[5];
                                String purchaseCount = strings[6];

                                for(Category categoryIn : categories){
                                    if(categoryIn.categoryCode.equalsIgnoreCase(category)){
                                        try{
                                            int slotInteger = Integer.parseInt(slot);
                                            int sellPriceInteger = Integer.parseInt(sellPrice);
                                            int purchasePriceInteger = Integer.parseInt(purchasePrice);
                                            int sellCountInteger = Integer.parseInt(sellCount);
                                            int purchaseCountInteger = Integer.parseInt(purchaseCount);

                                            Item item = new Item(player.getInventory().getItemInMainHand().clone(), sellPriceInteger, purchasePriceInteger, purchaseCountInteger, sellCountInteger);
                                            categoryIn.addItem(player, item, slotInteger);
                                            return true;
                                        } catch (NumberFormatException e){
                                            player.sendMessage(EasyStore.prefix + "/상점 아이템추가 [카테고리] [슬롯] [판매금액] [구매금액] [판매개수] [구매개수]");
                                            return false;
                                        }
                                    }
                                }

                                player.sendMessage(EasyStore.prefix + "해당 카테고리가 존재하지 않습니다");
                                return false;
                            } else{
                                player.sendMessage(EasyStore.prefix + "/상점 아이템추가 [카테고리] [슬롯] [판매금액] [구매금액] [판매개수] [구매개수]");
                                return false;
                            }
                        } else if(strings[0].equalsIgnoreCase("카테고리추가")){
                            if(strings.length > 1){
                                for(Category category : categories){
                                    if(category.categoryCode.equalsIgnoreCase(strings[1])){
                                        player.sendMessage(EasyStore.prefix + "이미 존재하는 카테고리입니다");
                                        return false;
                                    }
                                }

                                Category category = new Category(strings[1]);
                                categories.add(category);
                                player.sendMessage(EasyStore.prefix + "카테고리를 추가했습니다");
                                return true;
                            }
                        } else{
                            for(Category category : categories){
                                Bukkit.getLogger().info(category.categoryCode);
                                if(category.categoryCode.equalsIgnoreCase(strings[0])){
                                    category.openCategory(player);
                                    return true;
                                }
                            }

                            player.sendMessage(EasyStore.prefix + "해당 카테고리는 존재하지 않습니다");
                        }
                    }
                }
            }
        }
        return false;
    }
}
