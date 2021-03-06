package de.fu_berlin.inf.dpp.net.internal.extensions;

import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import de.fu_berlin.inf.dpp.net.XStreamExtensionProvider;

/* *
 * 
 * @JTourBusStop 1, Creating custom network messages, Packet Extensions:
 * 
 * We have seen many attempts in the past where developers tried to
 * accomplish things with the existing Saros API which could not be solved
 * because the logic behind the API was lacking of information that was
 * either never present or was available to late.
 * 
 * This tour explain how to create custom messages that can be used to
 * exchange needed informations.
 * 
 * Saros uses XMPP packet extensions (data represented in XML) to exchange
 * its data as messages. This class is the base class to inherit from when
 * creating a new packet extension.
 */

public abstract class SarosPacketExtension {

    // keep this short as it is included in every packet extension !
    public static final String VERSION = "SPXV1";

    public static final String EXTENSION_NAMESPACE = "de.fu_berlin.inf.dpp";

    @XStreamAlias("v")
    @XStreamAsAttribute
    private final String version = VERSION;

    public abstract static class Provider<T extends SarosPacketExtension>
        extends XStreamExtensionProvider<T> {

        public Provider(String elementName, Class<?>... classes) {
            super(EXTENSION_NAMESPACE, elementName, classes);
        }

        @Override
        public PacketFilter getPacketFilter() {

            return new AndFilter(super.getPacketFilter(), new PacketFilter() {
                @Override
                public boolean accept(Packet packet) {
                    SarosPacketExtension extension = getPayload(packet);

                    return extension != null
                        && VERSION.equals(extension.version);
                }
            });
        }
    }
}
