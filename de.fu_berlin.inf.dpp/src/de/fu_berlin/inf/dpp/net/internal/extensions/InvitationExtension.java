package de.fu_berlin.inf.dpp.net.internal.extensions;

import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class InvitationExtension extends SarosPacketExtension {

    @XStreamAlias("nid")
    @XStreamAsAttribute
    final protected String negotiationID;

    public InvitationExtension(String negotiationID) {
        this.negotiationID = negotiationID;
    }

    public String getNegotiationID() {
        return negotiationID;
    }

    public abstract static class Provider<T extends InvitationExtension>
        extends SarosPacketExtension.Provider<T> {

        public Provider(String elementName, Class<?>... classes) {
            super(elementName, classes);
        }

        public PacketFilter getPacketFilter(final String invitationID) {

            return new AndFilter(super.getPacketFilter(), new PacketFilter() {
                @Override
                public boolean accept(Packet packet) {
                    InvitationExtension extension = getPayload(packet);

                    if (extension == null)
                        return false;

                    return invitationID.equals(extension.getNegotiationID());
                }
            });
        }
    }
}
