package protocolsupport.protocol.packet.middleimpl.readable.play.v_4_5_6;

import protocolsupport.protocol.packet.id.LegacyPacketId;
import protocolsupport.protocol.packet.middleimpl.readable.LegacyFixedLengthPassthroughReadableMiddlePacket;

public class PlayerFlyingPacket extends LegacyFixedLengthPassthroughReadableMiddlePacket {

	public PlayerFlyingPacket() {
		super(LegacyPacketId.Serverbound.PLAY_FLYING, Byte.BYTES);
	}

}
