package com.bedmen.odyssey.network;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CUpdateEnchantPacket implements IPacket<INewServerPlayNetHandler> {
    private int level;
    private int id;
    private int cost;

    public CUpdateEnchantPacket() {
    }

    @OnlyIn(Dist.CLIENT)
    public CUpdateEnchantPacket(int level, int id, int cost) {
        this.level = level;
        this.id = id;
        this.cost = cost;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void read(PacketBuffer buf) throws IOException {
        this.level = buf.readVarInt();
        this.id = buf.readVarInt();
        this.cost = buf.readVarInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void write(PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.level);
        buf.writeVarInt(this.id);
        buf.writeVarInt(this.cost);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void handle(INewServerPlayNetHandler handler) {
        handler.processUpdateEnchant(this);
    }

    public int getLevel() {
        return this.level;
    }

    public int getId() {
        return this.id;
    }

    public int getCost() {
        return this.cost;
    }
}
