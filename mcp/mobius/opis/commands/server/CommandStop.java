package mcp.mobius.opis.commands.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.server.OpisServerTickHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class CommandStop extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_stop";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		modOpis.profilerRun = false;
		ProfilerRegistrar.turnOff();

		if (icommandsender instanceof EntityPlayer)
			OpisServerTickHandler.instance.players.add((EntityPlayer)icommandsender);
		
		for (EntityPlayer player : OpisServerTickHandler.instance.players)
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis stopped."))), (Player)player);		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return true;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Ends a run before completion.";
	}	
	
}
