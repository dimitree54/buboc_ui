package logic

import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.ingredient.Resource
import cuboc.recipe.Recipe

sealed class SearchResult
class RecipeSearchResult(val recipe: Recipe) : SearchResult()
class RecipeInputSearchResult(val recipeInput: RecipeInput) : SearchResult()
class ResourceSearchResult(val resource: Resource) : SearchResult()
class IngredientSearchResult(val ingredient: Ingredient) : SearchResult()
class RecipeOutputSearchResult(val recipeOutput: RecipeOutput) : SearchResult()
