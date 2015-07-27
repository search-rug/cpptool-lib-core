package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Access;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.proto.Base;
import nl.rug.search.cpptool.runtime.impl.AccessMapper;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CxxFunctionData implements CxxFunction {
    private final Function base;
    private final Type type;
    private final Access access;
    private final boolean isStatic;
    private final boolean isVirtual;
    private final boolean isPureVirtual;

    private CxxFunctionData(Function base, Type type, Access access, boolean isStatic, boolean isVirtual, boolean isPureVirtual) {
        this.base = base;
        this.type = type;
        this.access = access;
        this.isStatic = isStatic;
        this.isVirtual = isVirtual;
        this.isPureVirtual = isPureVirtual;
    }

    public static CxxFunctionData build(Function base, Type type, Base.Access access, boolean isStatic, boolean isVirtual, boolean isPureVirtual) {
        return new CxxFunctionData(base, type, AccessMapper.mapAccess(access), isStatic, isVirtual, isPureVirtual);
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return this.type;
    }

    @Nonnull
    @Override
    public Access access() {
        return this.access;
    }

    @Override
    public boolean isVirtual() {
        return this.isVirtual;
    }

    @Override
    public boolean isPureVirtual() {
        return this.isPureVirtual;
    }

    @Override
    public boolean isStatic() {
        return this.isStatic;
    }

    @Nonnull
    @Override
    public Type returnType() {
        return this.base.returnType();
    }

    @Nonnull
    @Override
    public Optional<Location> body() {
        return this.base.body();
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.base.decl();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CxxFunction")
                .add("base", base)
                .add("class", parentClass())
                .add("access", access())
                .add("isStatic", isStatic())
                .add("isVirtual", isVirtual())
                .add("isPureVirtual", isPureVirtual())
                .toString();
    }
}
