package at.kessaft.getdown.utils;

public class PlayerData {
    private int coins, kills;

    public PlayerData() {
        coins = 0;
        kills = 0;
    }

    public int getCoins() {
        return coins;
    }

    public int getKills() {
        return kills;
    }

    public void addKill(){
        this.kills++;
    }

    public void addCoins(int coins){
        this.coins += coins;
    }

    public void removeCoins(int coins){
        this.coins -= coins;
        if (this.coins < 0)
            this.coins = 0;
    }
}
