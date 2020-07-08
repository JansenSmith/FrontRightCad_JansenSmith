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
		String limbName = d.getScriptingName()
		File legFile = null
		boolean mirror=true
		if(limbName.contentEquals("DefaultLeg3")||limbName.contentEquals("DefaultLeg4")){
			println "Mirror leg parts"
			mirror=false
		}
		TransformNR  legRoot= d.getRobotToFiducialTransform()
		def leftSide=false
		def rear = true
		if(legRoot.getY()>0){
			leftSide=true;
		}
		if(legRoot.getX()>0){
			rear=false;
		}
		
		if(limbName.contentEquals("Tail")){
			if(linkIndex >1)
				return allCad;
			if(linkIndex ==0){
				legFile = ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"LunaTailMount.stl");
	
			}
			if(linkIndex ==1){
				legFile = ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"LunaTail.stl");
			}
	
			
		}else if(limbName.contentEquals("Head")){
			if(linkIndex >1)
				return allCad;
			if(linkIndex ==0){
				legFile = ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"LunaHeadMount.stl");

			}
			if(linkIndex ==1){
				legFile = ScriptingEngine.fileFromGit(
				"https://github.com/OperationSmallKat/Luna.git",
				"LunaHead.stl");
			}
	
			if(linkIndex ==2)
				return allCad;
		}else{
			if(leftSide){
				if(linkIndex ==0){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaShoulderMirror.stl");
		
				}
				if(linkIndex ==1){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaKneeMirror.stl");
		
				}
		
				if(linkIndex ==2){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaFootMirror.stl");
				}
			}
			else{
				if(linkIndex ==0){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaShoulder.stl");
		
				}
				if(linkIndex ==1){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaKnee.stl");
	
				}
		
				if(linkIndex ==2){
					legFile = ScriptingEngine.fileFromGit(
					"https://github.com/OperationSmallKat/Luna.git",
					"LunaFoot.stl");
		
				}
			}
		}
		


		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		DHLink dh = dhLinks.get(linkIndex)
		// Hardware to engineering units configuration
		LinkConfiguration conf = d.getLinkConfiguration(linkIndex);
		// Engineering units to kinematics link (limits and hardware type abstraction)
		AbstractLink abstractLink = d.getAbstractLink(linkIndex);// Transform used by the UI to render the location of the object
		// Transform used by the UI to render the location of the object
		Affine manipulator = dh.getListener();

		
		// Load the .CSG from the disk and cache it in memory
		println "Loading " +legFile
		CSG body  = Vitamins.get(legFile)
		if(linkIndex ==0){
			//body=moveDHValues(body,dh)

			if(limbName.contentEquals("Head")){
				body=body
				.movex(55)
				.movez(-10)
			
			}	

			else if(limbName.contentEquals("Tail")){
				body=body
				.movex(-15)
				.movez(-6)
			
			}
			
			else{
				body=body

			}
				
		}
		if(linkIndex ==1){
			

			if(limbName.contentEquals("Head")){
				body=body
				.movex(50)
				.movez(-13)
			}else if(limbName.contentEquals("Tail")){
				body=body
				.movex(-150)
				.movez(-9)
			
			}else{
				body=body
			}
		}
		if(linkIndex ==2){
			body=body

		
		}
		
		body.setManipulator(manipulator);
	
		def parts = [body ] as ArrayList<CSG>
		for(int i=0;i<parts.size();i++){
			parts.get(i).setColor(javafx.scene.paint.Color.RED)
		}
		return parts;
		
	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();



		// Load the .CSG from the disk and cache it in memory
		CSG body  = Vitamins.get(ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"Body.stl"))
		CSG body2  = Vitamins.get(ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"Body Battery Cover.stl"))
		CSG srv  = Vitamins.get(ScriptingEngine.fileFromGit(
			"https://github.com/OperationSmallKat/Luna.git",
			"Servo Cover.stl"))
			.roty(90)
			.movex(20)
			.movez(-83)
			.movey(42.5)
			
		def movedSHoulder=[]
		for(DHParameterKinematics leg:b.getLegs()){
			Transform t = TransformFactory.nrToCSG(leg.getRobotToFiducialTransform())
			movedSHoulder.add(srv.transformed(t))
			movedSHoulder.add(new Cube(5).toCSG().transformed(t))
		}
			
		def myMovedLinks =[body,body2].collect{
			it.movez(100)
		} 
		
		myMovedLinks.addAll(movedSHoulder)
		
		for(def part:myMovedLinks){
			part.setManipulator(b.getRootListener());
			part.setColor(javafx.scene.paint.Color.WHITE)
		}
		

		return myMovedLinks;
	}
};
