package nl.rug.search.cpptool.runtime.util;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.util.ContextTools;

import java.util.Optional;
import java.util.Set;

public class LinkValidator {
    public static void validate(DeclContainer container) {
        final Set<DeclContext> primaryContexts = Sets.newIdentityHashSet();
        Iterables.addAll(primaryContexts, allContexts(container.context()));
        final Set<Declaration> primaryDecls = Sets.newIdentityHashSet();
        Iterables.addAll(primaryDecls, ContextTools.traverseDecls(container.context()));

        // Assertion: None of the contexts found in filetrees should be found in the primary tree
        final FluentIterable<DeclContext> duplicateContexts = FluentIterable
                .from(container.inputFiles())
                .transform(SourceFile::localContext)
                .transform(Optional::get)
                .transformAndConcat(LinkValidator::allContexts)
                .filter(primaryContexts::contains); //Check if they are a part of the primary tree

        if (duplicateContexts.size() > 0) {
            System.err.println("Assertion failed, duplicate contexts:");
            duplicateContexts.forEach(System.err::println);
        }

        // Assertion: All declarations should link back to the primary tree
        final FluentIterable<Declaration> danglingDecls = FluentIterable
                .from(container.inputFiles())
                .transform(SourceFile::localContext)
                .transform(Optional::get)
                .transformAndConcat(ContextTools::traverseDecls)
                .filter(Predicates.not(primaryDecls::contains)); //Check if they are not in the primary tree

        if (danglingDecls.size() > 0) {
            System.err.println("Assertion failed, dangling declarations:");
            danglingDecls.forEach(System.err::println);
        }
    }

    private static Iterable<DeclContext> allContexts(DeclContext root) {
        return FluentIterable.from(root.children()).append(root);
    }
}
