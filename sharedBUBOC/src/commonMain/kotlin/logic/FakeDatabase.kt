package logic

import cuboc.database.CUBOCDatabase
import cuboc.ingredient.*
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import cuboc.recipe.Scenario
import cuboc_core.cuboc.database.search.*
import utility.MeasureUnit

class FakeDatabase : CUBOCDatabase {
    private val potato = Resource("", Ingredient("Potato", MeasureUnit("kg")), 5.0)
    private val oil = Resource("", Ingredient("Oil", MeasureUnit("l")), 3.0)
    private val kitchen = Resource("", Ingredient("Kitchen", MeasureUnit("unit")), 1.0)
    private val pan = Resource("", Ingredient("Pan", MeasureUnit("unit")), 1.0)
    private val chief = Resource("", Ingredient("Chief", MeasureUnit("person")), 1.0)
    private val friedPotato = Resource("", Ingredient("Fried potato", MeasureUnit("kg")), 5.0)

    private val inputs = listOf(potato, oil, kitchen, pan, chief).map { RecipeInput(it.ingredient, it.amount, true) }
    private val outputs = listOf(friedPotato, kitchen, pan, chief).map { RecipeOutput(it.ingredient, it.amount, true) }
    private val recipe = Recipe("Fried potato", inputs.toSet(), outputs.toSet(), Instruction(30, "Fry the potato"))

    private val resources = mutableListOf(potato, oil, kitchen, pan, chief, friedPotato)
    private val recipes = mutableListOf(recipe)

    private fun searchRecipeByName(): List<Recipe> {
        return recipes
    }

    private fun searchResourceByName(): List<Resource> {
        return resources
    }

    private fun searchIngredientByName(): List<Ingredient> {
        val ingredientsOfResources = searchResourceByName().map { it.ingredient }.toSet()
        val ingredientsOfInputs = searchRecipeByInput().flatMap { it.inputs }.map { it.ingredient }.toSet()
        val ingredientsOfOutputs = searchRecipeByOutput().flatMap { it.outputs }.map { it.ingredient }.toSet()
        return (ingredientsOfResources + ingredientsOfInputs + ingredientsOfOutputs).toList()
    }

    private fun searchRecipeByInput(): List<Recipe> {
        return recipes
    }

    private fun searchRecipeByOutput(): List<Recipe> {
        return recipes
    }

    override suspend fun addRecipe(recipe: Recipe): Boolean {
        recipes.add(recipe)
        return true
    }

    private var lastId = 0
    private fun generateResourceId(): String {
        return lastId++.toString()
    }

    override suspend fun addResource(ingredient: Ingredient, amount: Double): Resource {
        val resource = Resource(generateResourceId(), ingredient, amount)
        resources.add(resource)
        return resource
    }

    override suspend fun search(request: SearchRequest): List<SearchResult> {
        return when (request.type) {
            SearchType.All -> {
                val resources = searchResourceByName().map { ResourceSearchResult(it) }
                val recipes = searchRecipeByName().map { RecipeSearchResult(it) }
                resources + recipes
            }

            SearchType.Ingredients -> searchIngredientByName().map { IngredientSearchResult(it) }
            else -> TODO()
        }
    }

    override suspend fun removeResource(resource: Resource): Boolean {
        resources.remove(resource)
        return true
    }

    override suspend fun removeRecipe(recipe: Recipe): Boolean {
        recipes.remove(recipe)
        return true
    }

    override suspend fun execute(scenario: Scenario): PieceOfResource? {
        TODO("Not yet implemented")
    }
}