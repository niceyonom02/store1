import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Category implements Listener {
    private HashMap<Integer, Item> items = new HashMap<>();
    public String categoryCode;
    private Inventory inventory;

    public Category(String categoryCode){
        Bukkit.getPluginManager().registerEvents(this, EasyStore.EasyStore);
        this.categoryCode = categoryCode;
        inventory = Bukkit.createInventory(null, 45, categoryCode);
        loadItems();
    }

    public void addItem(Player player, Item item, int slot){
        if(items.containsKey(slot)){
            player.sendMessage(EasyStore.prefix + "이미 아이템이 존재하는 슬롯입니다");
            return;
        }

        ItemStack itemToShow = item.getItem().clone();
        ItemMeta itemMeta = itemToShow.getItemMeta();

        if(itemMeta.getLore() != null){
            List<String> lore = itemToShow.getItemMeta().getLore();
            lore.add("");
            lore.add(EasyStore.prefix + "구매가격: " + (item.currentPurchasePrice == 0 ? "구매불가" : item.currentPurchasePrice));
            lore.add(EasyStore.prefix + "판매가격: " + (item.currentSellPrice == 0 ? "판매불가" : item.currentSellPrice));
            if(item.currentSellPrice != 0){
                lore.add(EasyStore.prefix + "판매개수: " + (item.amountPerSell)+ "개");
            }
            if(item.currentPurchasePrice != 0){
                lore.add(EasyStore.prefix + "구매개수: " + (item.amountPerPurchase)+ "개");
            }
            itemMeta.setLore(lore);
            itemToShow.setItemMeta(itemMeta);
        } else{
            ArrayList<String> lore = new ArrayList<>();
            lore.add(EasyStore.prefix + "구매가격: " + (item.currentPurchasePrice == 0 ? "구매불가" : item.currentPurchasePrice));
            lore.add(EasyStore.prefix + "판매가격: " + (item.currentSellPrice == 0 ? "판매불가" : item.currentSellPrice));
            if(item.currentSellPrice != 0){
                lore.add(EasyStore.prefix + "판매개수: " + (item.amountPerSell)+ "개");
            }
            if(item.currentPurchasePrice != 0){
                lore.add(EasyStore.prefix + "구매개수: " + (item.amountPerPurchase)+ "개");
            }
            itemMeta.setLore(lore);
            itemToShow.setItemMeta(itemMeta);
        }
        items.put(slot, item);
        inventory.setItem(slot, itemToShow);

        player.sendMessage(EasyStore.prefix + "아이템을 추가했습니다");
    }

    public void removeItem(Player player, int slot){
        if(items.containsKey(slot)){
            inventory.setItem(slot, null);
            items.remove(slot);
        } else{
            player.sendMessage(EasyStore.prefix+ "해당 아이템은 존재하지 않습니다");
        }
    }

    public void saveItems(){
        FileConfiguration config = EasyStore.EasyStore.getConfig();
        config.set("category", "");
        for(int slot : items.keySet()){
            config.set("category." + categoryCode + "." + slot + ".itemStack", items.get(slot).getItem());
            config.set("category." + categoryCode + "." + slot + ".basePurchasePrice", items.get(slot).currentPurchasePrice);
            config.set("category." + categoryCode + "." + slot + ".baseSellPrice", items.get(slot).currentSellPrice);
            config.set("category." + categoryCode + "." + slot + ".amountPerPurchase", items.get(slot).amountPerPurchase);
            config.set("category." + categoryCode + "." + slot + ".amountPerSell", items.get(slot).amountPerSell);
        }

        EasyStore.EasyStore.saveConfig();
        items.clear();
    }

    public void openCategory(Player player){
        player.openInventory(inventory);
    }

    public void loadItems(){
        FileConfiguration config = EasyStore.EasyStore.getConfig();

        if(config.isConfigurationSection("category." + categoryCode)){
            for(String key : config.getConfigurationSection("category." + categoryCode).getKeys(false)){
                ItemStack itemStack = config.getItemStack("category." + categoryCode + "." + key + ".itemStack");
                int basePurchasePrice = config.getInt("category." + categoryCode + "." + key + ".basePurchasePrice");
                int baseSellPrice = config.getInt("category." + categoryCode + "." + key + ".baseSellPrice");
                int amountPerPurchase = config.getInt("category." + categoryCode + "." + key + ".amountPerPurchase");
                int amountPerSell = config.getInt("category." + categoryCode + "." + key + ".amountPerSell");

                //Bukkit.getLogger().info();
                Item item = new Item(itemStack,  baseSellPrice, basePurchasePrice, amountPerPurchase, amountPerSell);

                ItemStack itemToShow = itemStack.clone();
                ItemMeta itemMeta = itemToShow.getItemMeta();

                if(itemMeta.getLore() != null){
                    List<String> lore = itemToShow.getItemMeta().getLore();
                    lore.add("");
                    lore.add(EasyStore.prefix + "구매가격: " + (item.currentPurchasePrice == 0 ? "구매불가" : item.currentPurchasePrice));
                    lore.add(EasyStore.prefix + "판매가격: " + (item.currentSellPrice == 0 ? "판매불가" : item.currentSellPrice));
                    if(item.currentSellPrice != 0){
                        lore.add(EasyStore.prefix + "판매개수: " + (item.amountPerSell) + "개");
                    }
                    if(item.currentPurchasePrice != 0){
                        lore.add(EasyStore.prefix + "구매개수: " + (item.amountPerPurchase)+ "개");
                    }
                    itemMeta.setLore(lore);
                    itemToShow.setItemMeta(itemMeta);
                } else{
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(EasyStore.prefix + "구매가격: " + (item.currentPurchasePrice == 0 ? "구매불가" : item.currentPurchasePrice));
                    lore.add(EasyStore.prefix + "판매가격: " + (item.currentSellPrice == 0 ? "판매불가" : item.currentSellPrice));
                    if(item.currentSellPrice != 0){
                        lore.add(EasyStore.prefix + "판매개수: " + (item.amountPerSell)+ "개");
                    }
                    if(item.currentPurchasePrice != 0){
                        lore.add(EasyStore.prefix + "구매개수: " + (item.amountPerPurchase)+ "개");
                    }
                    itemMeta.setLore(lore);
                    itemToShow.setItemMeta(itemMeta);
                }
                int slot = Integer.parseInt(key);
                items.put(slot, item);
                inventory.setItem(slot, itemToShow);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().equalsIgnoreCase(categoryCode)){
            e.setCancelled(true);

            if(player.isOp()){
                if(e.isShiftClick()){
                    if(e.getRawSlot() < 54 && e.getRawSlot() >= 0){
                        removeItem(player, e.getRawSlot());
                    }
                }
            }

            if(items.containsKey(e.getRawSlot())){
                if(e.getClick().isLeftClick()){
                    purchase(items.get(e.getRawSlot()), player);
                } else if(e.getClick().isRightClick()){
                    sell(items.get(e.getRawSlot()), player);
                }
            }
        }
    }

    public void purchase(Item item, Player player){
        if(!item.isPurchaseForbid()){
            if(EasyStore.EasyStore.getEconomy().getCashManager().hasBalance(player.getUniqueId(), item.currentPurchasePrice)){
                EasyStore.EasyStore.getEconomy().getCashManager().withdraw(player.getUniqueId(), item.currentPurchasePrice);
                ItemStack giveItem = item.getItem().clone();
                giveItem.setAmount(item.amountPerPurchase);
                player.getInventory().addItem(giveItem);
                player.sendMessage(EasyStore.prefix + (item.getItem().getItemMeta().getDisplayName() == null ? item.getItem().getType().name() : item.getItem().getItemMeta().getDisplayName()) + " 아이템을 " + item.currentPurchasePrice + "N에 구입하였습니다!");
            } else{
                player.sendMessage(EasyStore.prefix + "돈이 부족합니다!");
            }
        } else{
            player.sendMessage(EasyStore.prefix + "판매 전용 아이템입니다!");
        }
    }

    public void sell(Item item, Player player){
        if(!item.isSellForbid()){
            if(isPlayerhasEnoughItem(player, item.getItem(), item.amountPerSell)){
                removePlayerItem(player, item.getItem(), item.amountPerSell);
                EasyStore.EasyStore.getEconomy().getCashManager().deposit(player.getUniqueId(), item.currentSellPrice);
                player.sendMessage(EasyStore.prefix +  (item.getItem().getItemMeta().getDisplayName() == null ? item.getItem().getType().name() : item.getItem().getItemMeta().getDisplayName()) + " 아이템을 " + item.currentSellPrice + "N에 판매하였습니다!");
            } else{
                player.sendMessage(EasyStore.prefix + "물건이 부족합니다!");
            }
        } else{
            player.sendMessage(EasyStore.prefix + "구매 전용 아이템입니다!");
        }
    }

    private boolean isPlayerhasEnoughItem(Player player, ItemStack itemStack, int minimumCount){
        int count = 0;
        for(int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack targetItem = player.getInventory().getItem(i);

            if(targetItem != null){
                if(targetItem.isSimilar(itemStack)){
                    count += targetItem.getAmount();
                }
            }
        }
        return count >= minimumCount;
    }

    private void removePlayerItem(Player player, ItemStack itemStack, int count){
        int removedCount = 0;
        for(int i = 0; i< player.getInventory().getSize();i++){
            ItemStack targetItem = player.getInventory().getItem(i);

            if(targetItem != null){
                if(targetItem.isSimilar(itemStack)){

                        if(removedCount + targetItem.getAmount() < count){
                            removedCount += targetItem.getAmount();
                            player.getInventory().setItem(i, null);
                        } else if(removedCount + targetItem.getAmount() == count){
                            player.getInventory().setItem(i, null);
                            break;
                        } else{
                            targetItem.setAmount((removedCount + targetItem.getAmount()) - count);
                            break;
                        }

                }
            }
        }
    }
}
