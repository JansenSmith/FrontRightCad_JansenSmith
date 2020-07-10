import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import com.neuronrobotics.sdk.addons.kinematics.AbstractLink
import com.neuronrobotics.sdk.addons.kinematics.DHLink
import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics
import com.neuronrobotics.sdk.addons.kinematics.LinkConfiguration
import com.neuronrobotics.sdk.addons.kinematics.MobileBase
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR

import java.nio.file.Paths;

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.FileUtil;
import eu.mihosoft.vrl.v3d.Transform;
import javafx.scene.transform.Affine;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory;
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here

return new ICadGenerator(){
	
	private CSG moveDHValues(CSG incoming,DHLink dh ){
		TransformNR step = new TransformNR(dh.DhStep(0)).inverse()
		Transform move = TransformFactory.nrToCSG(step)
		return incoming.transformed(move)
		
	}

	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		ArrayList<CSG> allCad=new ArrayList<>();
		File legFile = null
		if(linkIndex ==0){
			legFile = ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"LunaShoulderMirror.stl");
			def body = Vitamins.get(legFile);
			allCad.add(body);

		}
		if(linkIndex ==1){
			legFile = ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"LunaKneeMirror.stl");
			def body = Vitamins.get(legFile);
			allCad.add(body);

		}

		if(linkIndex ==2){
			legFile = ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"LunaFootMirror.stl");
			def body = Vitamins.get(legFile);
			allCad.add(body);
		}
		
		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		DHLink dh = dhLinks.get(linkIndex)
		Affine manipulator = dh.getListener();
		
		for(def body: allCad)
		body.setManipulator(manipulator);
		
		return allCad;
		
	}
	
};
