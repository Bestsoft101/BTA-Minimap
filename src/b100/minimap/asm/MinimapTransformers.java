package b100.minimap.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import b100.asmloader.ClassTransformer;
import b100.utils.asm.ASMHelper;

public class MinimapTransformers {
	
	class MinecraftTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/Minecraft");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			transformRunTick(ASMHelper.findMethod(classNode, "runTick", null));
		}
		
		private void transformRunTick(MethodNode method) {
			InsnList insert = new InsnList();
			insert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onTick", "()V"));
			injectBeforeEnd(method, insert);
		}
	}
	
	class WorldRendererTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/WorldRenderer");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			transformUpdateCameraAndRender(ASMHelper.findMethod(classNode, "updateCameraAndRender", null));
		}
		
		private void transformUpdateCameraAndRender(MethodNode method) {
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new VarInsnNode(Opcodes.FLOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onRender", "(F)V"));
			injectBeforeEnd(method, insnList);
		}
	}
	
	class RenderGlobalTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/RenderGlobal");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			transformLoadRenderers(ASMHelper.findMethod(classNode, "loadRenderers", null));
		}
		
		private void transformLoadRenderers(MethodNode method) {
			InsnList insnList = new InsnList();
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onLoadRenderers", "()V"));
			injectBeforeEnd(method, insnList);
		}
	}
	
	class GameSettingsTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/option/GameSettings");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			transformOptionChanged(ASMHelper.findMethod(classNode, "optionChanged", null));
		}
		
		private void transformOptionChanged(MethodNode method) {
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onOptionChanged", "(Lnet/minecraft/client/option/GameSettings;Lnet/minecraft/client/option/Option;)V"));
			injectBeforeEnd(method, insnList);
		}
		
	}
	
	class RenderEngineTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/RenderEngine");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			transformRefreshTextures(ASMHelper.findMethod(classNode, "refreshTextures", null));
		}
		
		private void transformRefreshTextures(MethodNode method) {
			InsnList insnList = new InsnList();
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onRefreshTextures", "()V"));
			injectBeforeEnd(method, insnList);
		}
	}
	
	public static boolean injectBeforeEnd(MethodNode method, InsnList instructionsToInsert) {
		for(int i = method.instructions.size() - 1; i >= 0; i--) {
			AbstractInsnNode instruction = method.instructions.get(i);
			if(instruction.getOpcode() == Opcodes.RETURN) {
				System.out.println("Inserting "+instructionsToInsert.size()+" instructions before '"+ASMHelper.toString(instruction)+"' at pos "+i);
				method.instructions.insertBefore(instruction, instructionsToInsert);
				return true;
			}
		}
		return false;
	}
	
}
