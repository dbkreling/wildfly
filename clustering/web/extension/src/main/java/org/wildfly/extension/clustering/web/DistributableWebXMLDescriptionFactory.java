/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.extension.clustering.web;

import java.util.function.Function;
import java.util.stream.Stream;

import org.jboss.as.clustering.controller.Attribute;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PersistentResourceXMLDescription;
import org.jboss.as.controller.PersistentResourceXMLDescription.PersistentResourceXMLBuilder;

/**
 * XML description factory for the distributable-web subsystem.
 * @author Paul Ferraro
 */
public enum DistributableWebXMLDescriptionFactory implements Function<DistributableWebSchema, PersistentResourceXMLDescription> {
    INSTANCE;

    @Override
    public PersistentResourceXMLDescription apply(DistributableWebSchema schema) {
        return builder(DistributableWebResourceDefinition.PATH, schema.getUri(), Attribute.stream(DistributableWebResourceDefinition.Attribute.class))
                .addChild(getInfinispanSessionManagementResourceXMLBuilder(schema))
                .addChild(getHotRodSessionManagementResourceXMLBuilder(schema))
                .addChild(builder(InfinispanSSOManagementResourceDefinition.WILDCARD_PATH, Attribute.stream(InfinispanSSOManagementResourceDefinition.Attribute.class)))
                .addChild(builder(HotRodSSOManagementResourceDefinition.WILDCARD_PATH, Attribute.stream(HotRodSSOManagementResourceDefinition.Attribute.class)))
                .addChild(builder(LocalRoutingProviderResourceDefinition.PATH).setXmlElementName("local-routing"))
                .addChild(builder(InfinispanRoutingProviderResourceDefinition.PATH, Attribute.stream(InfinispanRoutingProviderResourceDefinition.Attribute.class)).setXmlElementName("infinispan-routing"))
                .build();
    }

    private static PersistentResourceXMLBuilder getInfinispanSessionManagementResourceXMLBuilder(DistributableWebSchema schema) {
        PersistentResourceXMLBuilder builder = builder(InfinispanSessionManagementResourceDefinition.WILDCARD_PATH, Stream.concat(Attribute.stream(InfinispanSessionManagementResourceDefinition.Attribute.class), Attribute.stream(SessionManagementResourceDefinition.Attribute.class)));
        addAffinityChildren(builder).addChild(builder(PrimaryOwnerAffinityResourceDefinition.PATH).setXmlElementName("primary-owner-affinity"));
        if (schema.since(DistributableWebSchema.VERSION_2_0)) {
            builder.addChild(builder(RankedAffinityResourceDefinition.PATH, Attribute.stream(RankedAffinityResourceDefinition.Attribute.class)).setXmlElementName("ranked-affinity"));
        }
        return builder;
    }

    private static PersistentResourceXMLBuilder getHotRodSessionManagementResourceXMLBuilder(DistributableWebSchema schema) {
        return addAffinityChildren(builder(HotRodSessionManagementResourceDefinition.WILDCARD_PATH, Stream.concat(Attribute.stream(HotRodSessionManagementResourceDefinition.Attribute.class), Attribute.stream(SessionManagementResourceDefinition.Attribute.class))));
    }

    private static PersistentResourceXMLBuilder addAffinityChildren(PersistentResourceXMLBuilder builder) {
        return builder
                .addChild(builder(NoAffinityResourceDefinition.PATH).setXmlElementName("no-affinity"))
                .addChild(builder(LocalAffinityResourceDefinition.PATH).setXmlElementName("local-affinity"))
                ;
    }

    // TODO Drop methods below once WFCORE-6218 is available
    static PersistentResourceXMLDescription.PersistentResourceXMLBuilder builder(PathElement path) {
        return builder(path, Stream.empty());
    }

    static PersistentResourceXMLDescription.PersistentResourceXMLBuilder builder(PathElement path, Stream<? extends AttributeDefinition> attributes) {
        return builder(path, null, attributes);
    }

    static PersistentResourceXMLDescription.PersistentResourceXMLBuilder builder(PathElement path, String namespaceUri, Stream<? extends AttributeDefinition> attributes) {
        PersistentResourceXMLDescription.PersistentResourceXMLBuilder builder = PersistentResourceXMLDescription.builder(path, namespaceUri);
        attributes.forEach(builder::addAttribute);
        return builder;
    }
}
