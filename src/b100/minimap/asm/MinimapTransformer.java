package b100.minimap.asm;

import static b100.minimap.asm.ASMHelper.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import b100.asmloader.ClassTransformer;

public class MinimapTransformer {
	
	public static class MinecraftTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/Minecraft");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			for(MethodNode method : classNode.methods) {
				if(method.name.equals("runTick")) {
					InsnList insnList = new InsnList();
					insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onTick", "()V"));
					injectBeforeEnd(method, insnList);
				}
			}
		}
	}
	
	public static class WorldRendererTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/WorldRenderer");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			for(MethodNode method : classNode.methods) {
				if(method.name.equals("updateCameraAndRender")) {
					InsnList insnList = new InsnList();
					insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insnList.add(new VarInsnNode(Opcodes.FLOAD, 1));
					insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onRender", "(F)V"));
					injectBeforeEnd(method, insnList);
				}
			}
		}
	}
	
	public static class RenderGlobalTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/RenderGlobal");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			for(MethodNode method : classNode.methods) {
				if(method.name.equals("loadRenderers")) {
					InsnList insnList = new InsnList();
					insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onLoadRenderers", "()V"));
					injectBeforeEnd(method, insnList);
				}
			}
		}
	}
	
	public static class GameSettingsTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/option/GameSettings");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			for(MethodNode method : classNode.methods) {
				if(method.name.equals("optionChanged")) {
					InsnList insnList = new InsnList();
					insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
					insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onOptionChanged", "(Lnet/minecraft/client/option/GameSettings;Lnet/minecraft/client/option/Option;)V"));
					injectBeforeEnd(method, insnList);
				}
			}
		}
		
	}
	
	public static class RenderEngineTransformer extends ClassTransformer {

		@Override
		public boolean accepts(String className) {
			return className.equals("net/minecraft/client/render/RenderEngine");
		}

		@Override
		public void transform(String className, ClassNode classNode) {
			for(MethodNode method : classNode.methods) {
				if(method.name.equals("refreshTextures")) {
					InsnList insnList = new InsnList();
					insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "b100/minimap/asm/MinimapASM", "onRefreshTextures", "()V"));
					injectBeforeEnd(method, insnList);
				}
			}
		}
	}
	
}
