package nl.rug.search.cpptool.runtime.processor;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.CxxVariable;
import nl.rug.search.cpptool.api.data.Variable;
import nl.rug.search.cpptool.runtime.data.CxxVariableData;
import nl.rug.search.cpptool.runtime.data.ParamSetData;
import nl.rug.search.cpptool.runtime.data.VariableData;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.proto.Vars;

import java.util.Optional;

interface VarsProcessor {
    ProtobufProcessor<Vars.Var> VAR = (context, message) -> {
        final MDeclaration decl = context.createDeclaration(message.getName(), DeclType.VARIABLE);

        final VariableData var = VariableData.build(
                decl,
                context.findType(message.getOwnType()),
                message.getKind()
        );
        decl.insertData(Variable.class, var);

        if (message.getKind() == Vars.Var.VarKind.PARAMETER) {
            //Context is a declaration that has parameters, this is required.
            ParamSetData.hackyAccess(decl.parentContext().definition().get()).addVar(var);
        }

        return Optional.of(decl);
    };

    ProtobufProcessor<Vars.ClassVar> CXX_VAR = (context, message) -> {
        final MDeclaration impl = VAR.process(context, message.getBase()).get();

        impl.insertData(CxxVariable.class, CxxVariableData.build(
                impl.dataUnchecked(Variable.class),
                context.findType(message.getParent())
        ));

        return Optional.of(impl);
    };
}
