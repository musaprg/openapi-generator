package org.openapitools.codegen.languages;

import org.openapitools.codegen.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.parameters.Parameter;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZigClientCodegen extends DefaultCodegen implements CodegenConfig {
    public static final String PROJECT_NAME = "projectName";

    private final Logger LOGGER = LoggerFactory.getLogger(ZigClientCodegen.class);

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "zig";
    }

    public String getHelp() {
        return "Generates a zig client.";
    }

    public ZigClientCodegen() {
        super();

        outputFolder = "generated-code" + File.separator + "zig";
        modelTemplateFiles.put("model.mustache", ".zig");
        apiTemplateFiles.put("api.mustache", ".zig");
        embeddedTemplateDir = templateDir = "zig";
        apiPackage = "api";
        modelPackage = "model";
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));

        reservedWords = new HashSet<>(
            Arrays.asList(
                // Zig reserved words
                // https://github.com/ziglang/zig-spec/blob/1029b822dc5e62ac1e701ea832b59e8f757d245e/grammar/grammar.y#L511-L559
                "addrspace", "align", "allowzero", "and", "anyframe", "anytype", "asm", "async", "await", "break", "callconv", "catch", "comptime", "const", "continue", "defer", "else", "enum", "errdefer", "error", "export", "extern", "fn", "for", "if", "inline", "noalias", "nosuspend", "noinline", "opaque", "or", "orelse", "packed", "pub", "resume", "return", "linksection", "struct", "suspend", "switch", "test", "threadlocal", "try", "union", "unreachable", "usingnamespace", "var", "volatile", "while",
                // Zig primitive types
                // https://ziglang.org/documentation/master/#Primitive-Types
                "isize", "usize", "c_short", "c_ushort", "c_int", "c_uint", "c_long", "c_ulong", "c_longlong", "c_ulonglong", "c_longdouble", "f16", "f32", "f64", "f80", "f128", "bool", "anyopaque", "void", "noreturn", "type", "anyerror", "comptime_int", "comptime_float"
        ));

        typeMapping = new HashMap<>();
        typeMapping.put("integer", "i32");
        typeMapping.put("long", "i64");
        typeMapping.put("number", "f64");
        typeMapping.put("float", "f32");
        typeMapping.put("double", "f64");
        typeMapping.put("string", "[]const u8");
        // TODO: add support for other type mapping

        // Zig arbitrary bit-width integers
        // https://ziglang.org/documentation/master/#Primitive-Types
        // arbitrary bit-width integers can be referenced by using an identifier of i or u followed by digits. For example, the identifier i7 refers to a signed 7-bit integer. The maximum allowed bit-width of an integer type is 65535.
        for (int i = 0; i < (1<<16); i++) {
            String iStr = String.format("i%s", i);
            String uStr = String.format("u%s", i);
            reservedWords.add(iStr);
            reservedWords.add(uStr);
        }
        

    }
}
