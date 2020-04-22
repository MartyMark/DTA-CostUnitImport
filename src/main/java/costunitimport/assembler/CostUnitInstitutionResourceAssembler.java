package costunitimport.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import costunitimport.controller.ImportCostUnitFilesController;
import costunitimport.model.CostUnitInstitution;

@Component
public class CostUnitInstitutionResourceAssembler implements RepresentationModelAssembler<CostUnitInstitution, EntityModel<CostUnitInstitution>>{
	
	/**
	 * Hier werden die Domainklassen in das EnitiyModel gewrappt.<br>
	 * <code>linkTo(methodOn(..</code> erstellt die Links.
	 */
	@Override
	public EntityModel<CostUnitInstitution> toModel(final CostUnitInstitution entity) {
		return new EntityModel<>(entity,
			      linkTo(methodOn(ImportCostUnitFilesController.class).findCostUnitInstitutionById(entity.getId())).withSelfRel());
	}

}
