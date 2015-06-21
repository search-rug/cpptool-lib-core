package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.util.ContextPath;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

class InternalType implements MType {
    private final String name;
    private final Optional<Location> location;
    private final boolean isStronglyDefined;
    private final DynamicLookup<MDeclaration> decl;

    public InternalType(Base.ScopedName name, DynamicLookup<MDeclaration> decl, Optional<Location> location, boolean isStronglyDefined) {
        this.name = simplify(name);
        this.decl = decl;
        this.location = location;
        this.isStronglyDefined = isStronglyDefined;
    }

    private static String simplify(Base.ScopedName name) {
        if (name.hasContext()) {
            return Joiner.on("::").join(Iterables.concat(
                    ImmutableList.of(""),
                    ContextPath.from(name).segments(),
                    ImmutableList.of(name.getName())
            ));
        } else {
            return name.getName();
        }
    }

    @Nonnull
    @Override
    public String name() {
        return this.name;
    }

    @Nonnull
    @Override
    public Optional<Declaration> declaration() {
        return coerce(this.decl.toOptional());
    }

    @Nonnull
    @Override
    public Optional<Location> location() {
        return this.location;
    }

    @Override
    public boolean isStronglyDefined() {
        return this.isStronglyDefined;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("stronglyDefined", isStronglyDefined)
                .toString();
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> decl() {
        return this.decl;
    }
}
