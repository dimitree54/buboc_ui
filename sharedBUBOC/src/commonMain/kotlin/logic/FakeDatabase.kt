package logic

import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.ingredient.Resource
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import utility.MeasureUnit

class FakeDatabase {
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

    fun searchRecipeByName(name: String): List<Recipe> {
        return recipes
    }

    fun searchResourceByName(name: String): List<Resource> {
        return resources
    }

    fun searchIngredientByName(name: String): List<Ingredient> {
        val ingredientsOfResources = searchResourceByName(name).map { it.ingredient }.toSet()
        val ingredientsOfInputs = searchRecipeByInput(name).flatMap { it.inputs }.map { it.ingredient }.toSet()
        val ingredientsOfOutputs = searchRecipeByOutput(name).flatMap { it.outputs }.map { it.ingredient }.toSet()
        return (ingredientsOfResources + ingredientsOfInputs + ingredientsOfOutputs).toList()
    }

    fun searchRecipeByInput(name: String): List<Recipe> {
        return recipes
    }

    fun searchRecipeByOutput(name: String): List<Recipe> {
        return recipes
    }

    fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
    }

    private var lastId = 0
    private fun generateResourceId(): String {
        return lastId++.toString()
    }

    fun addResource(ingredient: Ingredient, amount: Double) {
        val resource = Resource(generateResourceId(), ingredient, amount)
        resources.add(resource)
    }

    fun search(request: SearchRequest): List<SearchResult> {
        val query = request.query ?: return emptyList()
        when (request.type) {
            SearchType.All -> {
                val resources = searchResourceByName(query).map { ResourceSearchResult(it) }
                val recipes = searchRecipeByName(query).map { RecipeSearchResult(it) }
                return resources + recipes
            }

            SearchType.Ingredients -> return searchIngredientByName(query).map { IngredientSearchResult(it) }
            else -> TODO()
        }
    }

    fun removeResource(resource: Resource) {
        resources.remove(resource)
    }

    fun removeRecipe(recipe: Recipe) {
        recipes.remove(recipe)
    }
}