package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableSet;
import nl.rug.search.cpptool.api.data.*;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public enum DeclType {
    VARIABLE(HasContext.FALSE, Variable.class, Named.class),
    FUNCTION(HasContext.TRUE, Function.class, ParamSet.class, Named.class, ContextHolder.class),
    TYPEDEF(HasContext.FALSE, Named.class, Typedef.class),
    RECORD(HasContext.TRUE, Record.class, Named.class, ContextHolder.class),
    ENUM(HasContext.TRUE, Named.class, ContextHolder.class),
    LAMBDA_FUNCTION(HasContext.TRUE, ParamSet.class, ContextHolder.class);

    public final HasContext hasContext;
    private final Set<Class<?>> dataTypes;

    DeclType(final HasContext context, final @Nonnull Class<?>... dataTypes) {
        this.hasContext = context;
        this.dataTypes = ImmutableSet.copyOf(dataTypes);
    }

    public boolean check(final @Nonnull Declaration decl) {
        checkNotNull(decl, "decl == NULL");
        return dataTypes.stream().allMatch(decl::has);
    }

    public enum HasContext {
        TRUE,
        FALSE
    }
}
