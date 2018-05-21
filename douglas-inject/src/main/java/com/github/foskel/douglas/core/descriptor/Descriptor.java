package com.github.foskel.douglas.core.descriptor;

import com.github.foskel.douglas.core.traits.Named;
import com.github.foskel.douglas.core.traits.Versioned;
import com.github.foskel.douglas.core.version.Version;

public interface Descriptor extends Named, Versioned, ArtifactDescriptor {

    @Override
    String getGroupId();

    @Override
    String getArtifactId();

    @Override
    Version getVersion();

    @Override
    String getName();
}