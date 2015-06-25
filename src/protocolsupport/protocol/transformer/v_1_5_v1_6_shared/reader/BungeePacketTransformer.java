package protocolsupport.protocol.transformer.v_1_5_v1_6_shared.reader;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.transformer.TransformedPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.ChatPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.EncryptionRequestPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.KeepAlivePacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.KickPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.LoginPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.PlayerListItemPacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.PluginMessagePacket;
import protocolsupport.protocol.transformer.v_1_5_v1_6_shared.packets.RespawnPacket;
import protocolsupport.utils.PingSerializer;
import protocolsupport.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.KeepAlive;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.Login;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.PlayerListHeaderFooter;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.Respawn;
import net.md_5.bungee.protocol.packet.StatusResponse;
import net.md_5.bungee.protocol.packet.Title;

public class BungeePacketTransformer {

	public static TransformedPacket[] transformBungeePacket(Channel channel, DefinedPacket packet, ByteBuf buf) {
		if (packet instanceof KeepAlive) {
			return new TransformedPacket[] { new KeepAlivePacket(((KeepAlive) packet).getRandomId()) };
		}
		if (packet instanceof PluginMessage) {
			PluginMessage pmessage = (PluginMessage) packet;
			return new TransformedPacket[] { new PluginMessagePacket(pmessage.getTag(), pmessage.getData().clone(), pmessage.isAllowExtendedPacket()) };
		}
		if (packet instanceof Chat) {
			Chat chat = (Chat) packet;
			return new TransformedPacket[] { new ChatPacket(Utils.toLegacyText(chat.getMessage())) };
		}
		if (packet instanceof Respawn) {
			Respawn respawn = (Respawn) packet;
			return new TransformedPacket[] { new RespawnPacket(respawn.getDimension(), respawn.getDifficulty(), respawn.getGameMode(), respawn.getLevelType()) };
		}
		if (packet instanceof PlayerListItem) {
			PlayerListItem listitem = (PlayerListItem) packet;
			TransformedPacket[] packets = new TransformedPacket[listitem.getItems().length];
			for (int i = 0; i < packets.length; i++) {
				packets[i] = new PlayerListItemPacket(listitem.getAction(), listitem.getItems()[i]);
			}
			return packets;
		}
		if (packet instanceof Kick) {
			Kick kick = (Kick) packet;
			return new TransformedPacket[] { new KickPacket(Utils.toLegacyText(kick.getMessage())) };
		}
		if (packet instanceof LoginSuccess || packet instanceof Title || packet instanceof PlayerListHeaderFooter) {
			return new TransformedPacket[0];
		}
		if (packet instanceof StatusResponse) {
			StatusResponse status = (StatusResponse) packet;
			return new TransformedPacket[] { new KickPacket(PingSerializer.fromJSON(ProtocolSupportAPI.getProtocolVersion(channel.remoteAddress()).getId(), status.getResponse())) };
		}
		if (packet instanceof EncryptionRequest) {
			EncryptionRequest erequest = (EncryptionRequest) packet;
			return new TransformedPacket[] { new EncryptionRequestPacket(erequest.getServerId(), erequest.getVerifyToken()) };
		}
		if (packet instanceof Login) {
			Login login = (Login) packet;
			return new TransformedPacket[] { new LoginPacket(login.getEntityId(), login.getGameMode(), (byte) login.getDimension(), login.getDifficulty(), login.getMaxPlayers(), login.getLevelType()) };
		}
		return null;
	}

}
