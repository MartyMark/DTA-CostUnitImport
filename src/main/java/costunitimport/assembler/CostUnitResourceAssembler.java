package costunitimport.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import costunitimport.controller.CostUnitController;
import costunitimport.model.CostUnit;

@Component
public class CostUnitResourceAssembler implements RepresentationModelAssembler<CostUnit, EntityModel<CostUnit>>{
	
	/**
	 * Hier werden die Domainklassen in das EnitiyModel gewrappt.<br>
	 * <code>linkTo(methodOn(..</code> erstellt die Links.
	 */
	@Override
	public EntityModel<CostUnit> toModel(final CostUnit entity) {
		return new EntityModel<>(entity,
			      linkTo(methodOn(CostUnitController.class).findCostUnitById(entity.getId())).withSelfRel());
	}

}
