package b100.minimap.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMHelper {
	
	public static ClassNode getClassNode(byte[] bytes) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		return classNode;
	}
	
	public static byte[] getBytes(ClassNode classNode) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}
	
	public static String getOpcodeName(int opcode) {
		if(opcode == Opcodes.RETURN) return "RETURN";

		if(opcode == Opcodes.LDC) return "LDC";
		if(opcode == Opcodes.SIPUSH) return "SIPUSH";
		
		if(opcode == Opcodes.GETFIELD) return "GETFIELD";
		
		if(opcode == Opcodes.INVOKEVIRTUAL) return "INVOKEVIRTUAL";
		if(opcode == Opcodes.INVOKESPECIAL) return "INVOKESPECIAL";
		if(opcode == Opcodes.INVOKESTATIC) return "INVOKESTATIC";
		if(opcode == Opcodes.INVOKEDYNAMIC) return "INVOKEDYNAMIC";
		
		return null;
	}
	
	public static void printInstructions(MethodNode method) {
		InsnList instructions = method.instructions;
		for(int i=0; i < instructions.size(); i++) {
			AbstractInsnNode instruction = instructions.get(i); 
			
			if(instruction instanceof LineNumberNode) {
				LineNumberNode node = (LineNumberNode) instruction;
				System.out.println(node.line+":");
			}else {
				System.out.println("  " + toString(instruction));	
			}
		}
	}
	
	public static String toString(AbstractInsnNode instruction) {
		int opcode = instruction.getOpcode();

		String type = instruction.getClass().getSimpleName();
		String opcodeName = getOpcodeName(opcode);
		
		if(opcodeName != null) {
			type = opcodeName;
		}

		if(instruction instanceof LabelNode) {
			LabelNode node = (LabelNode) instruction;
			type = type + " " + node.getLabel();
		}
		if(instruction instanceof FrameNode) {
			FrameNode node = (FrameNode) instruction;
			type = type + " " + node.type;
		}
		if(instruction instanceof LineNumberNode) {
			LineNumberNode node = (LineNumberNode) instruction;
			type = type + " " + node.line;
		}
		if(instruction instanceof FieldInsnNode) {
			FieldInsnNode node = (FieldInsnNode) instruction;
			type = type + " " + node.owner + " " + node.name + " " + node.desc;
		}
		if(instruction instanceof MethodInsnNode) {
			MethodInsnNode node = (MethodInsnNode) instruction;
			type = type + " " + node.owner + " " + node.name + " " + node.desc;
		}
		if(instruction instanceof LdcInsnNode) {
			LdcInsnNode node = (LdcInsnNode) instruction;
			type = type + " " + node.cst;
		}
		if(instruction instanceof VarInsnNode) {
			VarInsnNode node = (VarInsnNode) instruction;
			type = type + " " + node.var;
		}
		if(instruction instanceof JumpInsnNode) {
			JumpInsnNode node = (JumpInsnNode) instruction;
			type = type + " " + toString(node.label);
		}
		
		return opcode+" "+type;
	}
	
	public static boolean injectAtStart(MethodNode method, AbstractInsnNode instruction) {
		method.instructions.insert(method.instructions.getFirst(), instruction);
		return true;
	}
	
	public static boolean injectBeforeEnd(MethodNode method, InsnList instructionsToInsert) {
		for(int i = method.instructions.size() - 1; i >= 0; i--) {
			AbstractInsnNode instruction = method.instructions.get(i);
			if(instruction.getOpcode() == Opcodes.RETURN) {
				System.out.println("Inserting "+instructionsToInsert.size()+" instructions before '"+toString(instruction)+"' at pos "+i);
				method.instructions.insertBefore(instruction, instructionsToInsert);
				return true;
			}
		}
		return false;
	}

}
