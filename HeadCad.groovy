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
		//println "\n\n\nNew limb "+d
		
		ArrayList<CSG> allCad=new ArrayList<>();
		File legFile = null
		if(linkIndex ==0){
			CSG base  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Tail Head Base.stl"))
				.rotz(180)
				.setColor(javafx.scene.paint.Color.DARKGREY)
			CSG cover1  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Tail Head Cover 1.stl"))
				.rotz(180)
				.setColor(javafx.scene.paint.Color.DARKGREY)
			CSG cover2  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Tail Head Cover 2.stl"))
				.rotz(180)
				.setColor(javafx.scene.paint.Color.YELLOW)
			allCad.add(base);
			allCad.add(cover1);
			allCad.add(cover2);

		}
		if(linkIndex ==1){
			CSG head  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Head.stl"))
				.rotz(180)
				.movez(3)
				.setColor(javafx.scene.paint.Color.DARKGREY)
			CSG dlimb  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Drive Limb.stl"))
				.rotz(180)
				.rotx(180)
				.movex(-60)
				.movez(-14)
				.setColor(javafx.scene.paint.Color.YELLOW)
			CSG hlimb  = Vitamins.get(ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"Head Limb.stl"))
				.movex(-60)
				.movez(20)
				.setColor(javafx.scene.paint.Color.YELLOW)
			allCad.add(head);
			allCad.add(dlimb);
			allCad.add(hlimb);
		}
		
		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		DHLink dh = dhLinks.get(linkIndex)
		// Hardware to engineering units configuration
		LinkConfiguration conf = d.getLinkConfiguration(linkIndex);
		// Engineering units to kinematics link (limits and hardware type abstraction)
		AbstractLink abstractLink = d.getAbstractLink(linkIndex);// Transform used by the UI to render the location of the object
		// Transform used by the UI to render the location of the object
		Affine manipulator = dh.getListener();
		
		for(CSG body: allCad)
			body.setManipulator(manipulator);
		
		return allCad;
		
	}

	@Override
	public ArrayList<CSG> generateBody(MobileBase arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
};
